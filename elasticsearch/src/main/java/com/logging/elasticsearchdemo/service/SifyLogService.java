package com.logging.elasticsearchdemo.service;


import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.logging.elasticsearchdemo.model.SifyLog;

public interface SifyLogService {
	public Page<SifyLog> getAllLogs(Pageable pageable);
	public List<SifyLog> getLogsByTimeStamp(Instant start,Instant end);
	public Page<SifyLog> sortByPriAsc(Pageable pageable);
	public Page<SifyLog> sortByPriDesc(Pageable pageable);
	public List<SifyLog> getSysLogPri(Integer syslogPri);
}
