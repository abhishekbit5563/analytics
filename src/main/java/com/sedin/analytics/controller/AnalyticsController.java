package com.sedin.analytics.controller;

import com.sedin.analytics.constant.TimeDuration;
import com.sedin.analytics.dto.AnalyticsRequestDTO;
import com.sedin.analytics.dto.AnalyticsResponseDTO;
import com.sedin.analytics.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController
{

    @Autowired
    private AnalyticsService analyticsService;

    /* V1 Starts */
    @PostMapping("/v1/record")
    public ResponseEntity<AnalyticsResponseDTO> recordHitV1(@RequestBody AnalyticsRequestDTO analyticsRequestDTO)
    {
        AnalyticsResponseDTO analyticsResponseDTO= analyticsService.updatePageUrlHitV1(analyticsRequestDTO,null);
        return ResponseEntity.ok(analyticsResponseDTO);
    }
    /* V1 Ends */

    /* V2 Starts */
    @PostMapping("/v2/record")
    public ResponseEntity<String> recordHitV2(@RequestBody AnalyticsRequestDTO analyticsRequestDTO)
    {
        analyticsService.updatePageUrlHitV2(analyticsRequestDTO);
        return ResponseEntity.ok("Successfully recorded !! ");
    }
    /* V2 Ends */

    @GetMapping("/report")
    public  ResponseEntity<Map<String, List<AnalyticsResponseDTO>>> fetchPageHitCount(@RequestParam TimeDuration timeDuration)
    {
        Map<String, List<AnalyticsResponseDTO>> resultMap= analyticsService.fetchPageHitCount(timeDuration);
        return ResponseEntity.ok(resultMap);
    }
}
