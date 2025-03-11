package com.sedin.analytics.helper;

import com.sedin.analytics.constant.TimeDuration;
import com.sedin.analytics.dto.AnalyticsResponseDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.*;

@Component
public class AnalyticsHelper
{

    public Map<String, List<AnalyticsResponseDTO>> groupResultList(TimeDuration timeDuration,List<AnalyticsResponseDTO> responseDTOs)
    {
        if(timeDuration.equals(TimeDuration.Daily))
        {
            return groupByDay(responseDTOs);
        }
        else if(timeDuration.equals(TimeDuration.Weekly))
        {
            return groupByWeek(responseDTOs);
        }
        else if(timeDuration.equals(TimeDuration.Monthly))
        {
            return groupByMonth(responseDTOs);
        }
        return null;
    }
    private Map<String, List<AnalyticsResponseDTO>> groupByDay(List<AnalyticsResponseDTO> responseDTOs)
    {
        Map<String, List<AnalyticsResponseDTO>> grouped = new TreeMap<>();
        for (AnalyticsResponseDTO item : responseDTOs)
        {
            LocalDateTime lastUpdatedDttm = item.getLastUpdatedDttm();
            // Extract YYYY-MM-DD
            String dateAsKey = lastUpdatedDttm.toString().substring(0, 10);
            grouped.computeIfAbsent(dateAsKey, k -> new ArrayList<>()).add(item);
        }
        return grouped;
    }

    private Map<String,List<AnalyticsResponseDTO>> groupByWeek(List<AnalyticsResponseDTO> responseDTOs)
    {
        Map<String, List<AnalyticsResponseDTO>> grouped = new TreeMap<>();
        for (AnalyticsResponseDTO item : responseDTOs)
        {
            LocalDateTime lastUpdatedDttm = item.getLastUpdatedDttm();
            int weekNumberAsKey = lastUpdatedDttm.get(WeekFields.of(Locale.getDefault()).weekOfYear());
            grouped.computeIfAbsent(String.valueOf(weekNumberAsKey), k -> new ArrayList<>()).add(item);
        }
        return grouped;
    }

    private Map<String,List<AnalyticsResponseDTO>> groupByMonth(List<AnalyticsResponseDTO> data)
    {
        Map<String,List<AnalyticsResponseDTO>> grouped = new TreeMap<>();
        for (AnalyticsResponseDTO item : data)
        {
            LocalDateTime lastUpdatedDttm = item.getLastUpdatedDttm();
            int monthNumber = lastUpdatedDttm.getMonthValue();
            grouped.computeIfAbsent(String.valueOf(monthNumber), k -> new ArrayList<>()).add(item);
        }
        return  grouped;
    }


}
