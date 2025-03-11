package com.sedin.analytics.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
public class AnalyticsResponseDTO
{
    private String pageUrl;

    private String region;

    private Long pageHitCount;

    private LocalDateTime lastUpdatedDttm;
}
