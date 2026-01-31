package com.logging.elasticsearchdemo.repository;

import java.time.Instant;
import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.logging.elasticsearchdemo.model.SifyLog;

public interface SifyLogRepository extends ElasticsearchRepository<SifyLog, String> {
    List<SifyLog> findByTimestampBetween(Instant start, Instant end);
    List<SifyLog> findBySyslogPri(Integer syslogPri);}
