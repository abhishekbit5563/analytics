package com.sedin.analytics.translator;

import com.sedin.analytics.dto.AnalyticsRequestDTO;
import com.sedin.analytics.dto.AnalyticsResponseDTO;
import com.sedin.analytics.entity.AnalyticsEntity;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsTranslator
{

    public AnalyticsResponseDTO covertToDTO(AnalyticsEntity analyticsEntity)
    {
        AnalyticsResponseDTO analyticsResponseDTO = new AnalyticsResponseDTO();
        analyticsResponseDTO.setPageUrl(analyticsEntity.getPageUrl());
        analyticsResponseDTO.setRegion(analyticsEntity.getRegion());
        analyticsResponseDTO.setPageHitCount(analyticsEntity.getPageHitCount());
        analyticsResponseDTO.setLastUpdatedDttm(analyticsEntity.getLastUpdatedDttm());
        return analyticsResponseDTO;
    }

    public AnalyticsEntity convertToEntity(AnalyticsRequestDTO analyticsRequestDTO)
    {
        AnalyticsEntity analyticsEntity = new AnalyticsEntity();
        analyticsEntity.setPageUrl(analyticsRequestDTO.getPageUrl());
        analyticsEntity.setRegion(analyticsRequestDTO.getRegion());
        analyticsEntity.setPageHitCount(new Long(1));
        return analyticsEntity;
    }
}
