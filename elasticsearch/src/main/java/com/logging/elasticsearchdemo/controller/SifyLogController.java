package com.logging.elasticsearchdemo.controller;


import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.logging.elasticsearchdemo.dto.SifyLogDTO;
import com.logging.elasticsearchdemo.mapper.SifyLogMapper;
import com.logging.elasticsearchdemo.model.SifyLog;
import com.logging.elasticsearchdemo.service.SifyLogService;


@RestController
@RequestMapping("/logs")
public class SifyLogController {

	private final SifyLogService sifyLogService;

	public SifyLogController(SifyLogService sifyLogService) {
		this.sifyLogService = sifyLogService;
	         }

	@GetMapping("/getAll")
	public List<SifyLogDTO> getAllLogs(@RequestParam(defaultValue = "0") int page,
	                                   @RequestParam(defaultValue = "10") int size) {
	    Pageable pageable = PageRequest.of(page, size);
	    Page<SifyLog> pageResult = sifyLogService.getAllLogs(pageable);

	    return pageResult.getContent().stream()
	            .map(SifyLogMapper::toDTO)
	            .toList();
	      }

	@GetMapping("/getLogByTimeRange")
	public List<SifyLogDTO> getLogsByTimeRange(
	    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant start,
	    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant end) {
	    
	    List<SifyLog> logs = sifyLogService.getLogsByTimeStamp(start, end);
	    return logs.stream()
	               .map(SifyLogMapper::toDTO)
	               .toList();
	          }
	
	@GetMapping("/sortSyslogPriAsc")
	public List<SifyLogDTO> sortBySyslogPriAscDetails(@RequestParam(defaultValue = "0") int page,
	                                           @RequestParam(defaultValue = "10") int size) {
	    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "syslogPri"));
	    Page<SifyLog> logs = sifyLogService.sortByPriAsc(pageable);
	    return logs.getContent().stream()
	            .map(SifyLogMapper::toDTO)
	            .toList();
	    }

	@GetMapping("/sortSyslogPriDesc")
	public List<SifyLogDTO> sortBySyslogPriDescDetails(@RequestParam(defaultValue = "0") int page,
	                                            @RequestParam(defaultValue = "10") int size) {
	    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "syslogPri"));
	    Page<SifyLog> logs = sifyLogService.sortByPriDesc(pageable);
	    return logs.getContent().stream()
	            .map(SifyLogMapper::toDTO)
	            .toList();	
	    }
	@GetMapping("/getByLevel")
	public List<SifyLogDTO> getBySifyLogPriDetails(@RequestParam Integer num){
		List<SifyLog> resultList= sifyLogService.getSysLogPri(num);
		return resultList.stream()
				.map(SifyLogMapper :: toDTO)
				.toList();
	}
}