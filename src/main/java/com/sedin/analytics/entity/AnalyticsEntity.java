package com.sedin.analytics.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "analytics")
@Setter
@Getter
public class AnalyticsEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "page_url",length = 255, nullable = false)
    private String pageUrl;

    @Column(name = "page_hit_count",nullable = false)
    private Long pageHitCount;

    @Column(name = "region",length = 255, nullable = false)
    private String region;

    @UpdateTimestamp
    @Column(name = "last_updated_dttm",nullable = false)
    private LocalDateTime lastUpdatedDttm;

}
