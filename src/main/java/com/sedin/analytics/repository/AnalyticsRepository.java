package com.sedin.analytics.repository;

import com.sedin.analytics.entity.AnalyticsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnalyticsRepository extends CrudRepository<AnalyticsEntity,Long>
{
    Optional<AnalyticsEntity> findByPageUrlAndRegion(String pageUrl,String region);

    List<AnalyticsEntity> findByLastUpdatedDttmBetweenOrderByLastUpdatedDttm(LocalDateTime from, LocalDateTime to);
}
