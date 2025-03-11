package com.sedin.analytics.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AnalyticsRequestDTO
{
    private String pageUrl;

    private String region;

}
