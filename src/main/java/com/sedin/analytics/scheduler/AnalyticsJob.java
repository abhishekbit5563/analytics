package com.sedin.analytics.scheduler;

import com.sedin.analytics.service.AnalyticsService;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AnalyticsJob
{
    @Autowired
    private AnalyticsService analyticsService;

    // every 6 seconds
    @Scheduled(fixedDelayString = "6000")
    @SchedulerLock(name = "AnalyticsJob")
    public void scheduledTask()
    {
        System.out.println("AnalyticsJob....");
        analyticsService.persistToDB();
    }
}
