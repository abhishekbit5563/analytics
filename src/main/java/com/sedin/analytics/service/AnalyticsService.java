package com.sedin.analytics.service;

import com.sedin.analytics.constant.TimeDuration;
import com.sedin.analytics.dto.AnalyticsRequestDTO;
import com.sedin.analytics.dto.AnalyticsResponseDTO;
import com.sedin.analytics.entity.AnalyticsEntity;
import com.sedin.analytics.helper.AnalyticsHelper;
import com.sedin.analytics.repository.AnalyticsRepository;
import com.sedin.analytics.translator.AnalyticsTranslator;
import com.sedin.analytics.utility.DateUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class AnalyticsService
{
    /* v1 : Starts */
    @Autowired
    private AnalyticsRepository analyticsRepository;

    @Autowired
    private AnalyticsTranslator analyticsTranslator;

    @Autowired
    private AnalyticsHelper analyticsHelper;

    @Transactional(Transactional.TxType.REQUIRED)
    public AnalyticsResponseDTO updatePageUrlHitV1(AnalyticsRequestDTO analyticsRequestDTO,Long pageHitCount)
    {
        AnalyticsResponseDTO responseDTO = null;
        AnalyticsEntity updatedAnalyticsEntity = null;
        String pageurl = analyticsRequestDTO.getPageUrl();
        String region =  analyticsRequestDTO.getRegion();
        Optional<AnalyticsEntity> analyticsEntityOptional = analyticsRepository.findByPageUrlAndRegion(pageurl,region);

        if(analyticsEntityOptional.isPresent())
        {
            AnalyticsEntity existingAnalyticsEntity = analyticsEntityOptional.get();
            if(pageHitCount != null)
            {
                existingAnalyticsEntity.setPageHitCount(existingAnalyticsEntity.getPageHitCount()+pageHitCount);
            }
            else
            {
                existingAnalyticsEntity.setPageHitCount(existingAnalyticsEntity.getPageHitCount()+1);
            }

            updatedAnalyticsEntity = analyticsRepository.save(existingAnalyticsEntity);
        }
        else
        {
            AnalyticsEntity newAnalyticsEntity =  analyticsTranslator.convertToEntity(analyticsRequestDTO);
            updatedAnalyticsEntity = analyticsRepository.save(newAnalyticsEntity);
        }
        responseDTO = analyticsTranslator.covertToDTO(updatedAnalyticsEntity);
        return responseDTO;
    }

    /* V1 Ends*/
    /* V2 Starts */
    /* TODO: change map to redis cache */
    private final ConcurrentHashMap<String, AtomicLong> countMap = new ConcurrentHashMap<>();

    public void updatePageUrlHitV2(AnalyticsRequestDTO analyticsRequestDTO)
    {
        String pageUrl = analyticsRequestDTO.getPageUrl();
        String region =  analyticsRequestDTO.getRegion();
        String countMapKey = pageUrl+"##"+region;
        countMap.computeIfAbsent(countMapKey, k -> new AtomicLong(0)).incrementAndGet();
    }

    public void persistToDB()
    {
        for (Map.Entry<String, AtomicLong> entry : countMap.entrySet())
        {
            String countMapKey = entry.getKey();
            String[]  countMapKeyArr = countMapKey.split("##");
            String pageUrl = countMapKeyArr[0];
            String region = countMapKeyArr[1];
            Long pageHitCount = entry.getValue().get();
            AnalyticsRequestDTO analyticsRequestDTO = new AnalyticsRequestDTO();
            analyticsRequestDTO.setPageUrl(pageUrl);
            analyticsRequestDTO.setRegion(region);
            updatePageUrlHitV1(analyticsRequestDTO,pageHitCount);

            // Reset countMap after persisting into DB
            entry.getValue().set(0);
        }
    }
    /* V2 Ends */

    @Transactional(Transactional.TxType.SUPPORTS)
    public  Map<String, List<AnalyticsResponseDTO>> fetchPageHitCount(TimeDuration timeDuration)
    {
        Map<String, List<AnalyticsResponseDTO>> resultMap =  null;

        // current date and time
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = null;
        if(timeDuration.equals(TimeDuration.Daily))
        {
            from = now.minusDays(90);
            from =DateUtility.atStartOfDay(from);
        }
        else if(timeDuration.equals(TimeDuration.Weekly))
        {
            from = now.minusWeeks(12);
            from =DateUtility.atStartOfDay(from);
        }
        else if(timeDuration.equals(TimeDuration.Monthly))
        {
            from = now.minusMonths(12);
            from =DateUtility.atStartOfDay(from);
        }

        List<AnalyticsEntity> resultList = analyticsRepository.findByLastUpdatedDttmBetweenOrderByLastUpdatedDttm(from,now);

        if(resultList != null)
        {
            List<AnalyticsResponseDTO> responseDTOs = new ArrayList<>();
            resultList.stream().forEach(analyticsEntity ->
                    responseDTOs.add(analyticsTranslator.covertToDTO(analyticsEntity)));
            resultMap = analyticsHelper.groupResultList(timeDuration,responseDTOs);
        }

        return resultMap;
    }
}
