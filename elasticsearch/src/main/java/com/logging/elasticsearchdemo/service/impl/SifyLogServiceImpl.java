package com.logging.elasticsearchdemo.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import com.logging.elasticsearchdemo.model.SifyLog;
import com.logging.elasticsearchdemo.repository.SifyLogRepository;
import com.logging.elasticsearchdemo.service.SifyLogService;

@Service
public class SifyLogServiceImpl implements SifyLogService {
	private final SifyLogRepository sifyLogRepository;

    public SifyLogServiceImpl(SifyLogRepository sifyLogRepository) {
        this.sifyLogRepository = sifyLogRepository;
    }

	@Override
	public Page<SifyLog> getAllLogs(Pageable pageable) {
		return sifyLogRepository.findAll(pageable);
	}

	@Override
	public List<SifyLog> getLogsByTimeStamp(Instant start, Instant end) {
		return sifyLogRepository.findByTimestampBetween(start, end);
	}

	@Override
	public Page<SifyLog> sortByPriAsc(Pageable pageable) {
		return sifyLogRepository.findAll(pageable);
	}

	@Override
	public Page<SifyLog> sortByPriDesc(Pageable pageable) {
		return sifyLogRepository.findAll(pageable);
	}

	@Override
	public List<SifyLog> getSysLogPri(Integer syslogPri) {
				return sifyLogRepository.findBySyslogPri(syslogPri);
	}

}
