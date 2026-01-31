package com.logging.elasticsearchdemo.mapper;

import com.logging.elasticsearchdemo.dto.SifyLogDTO;
import com.logging.elasticsearchdemo.model.SifyLog;

public class SifyLogMapper {

    public static SifyLogDTO toDTO(SifyLog log) {
        SifyLogDTO dto = new SifyLogDTO();
        dto.setId(log.getId());
        dto.setProgram(log.getProgram());
        dto.setSyslogPri(log.getSyslogPri());
        dto.setLog_message(log.getLog_message());
        dto.setType(log.getType());
        dto.setMessage(log.getMessage());
        dto.setPid(log.getPid());
        dto.setHostIp(log.getHost() != null ? log.getHost().getIp() : null);
        dto.setTimestamp(log.getTimestamp());
        dto.setEvent(log.getEvent());
        dto.setSeverity(getSeverityFromSyslogPri(log.getSyslogPri()));
        return dto;
    }
    
    public static String getSeverityFromSyslogPri(Integer pri) {
    		if(pri == null) 
    			return "UNKNOWN";
    		int severity = pri % 8;
    		return switch (severity) {
			 case 0 -> "EMERGENCY";
			 case 1 -> "ALERT";
			 case 2 -> "CRITICAL";
			 case 3 -> "ERROR";
			 case 4 -> "WARNING";
			 case 5 -> "NOTICE";
			 case 6 -> "INFO";
			 case 7 -> "DEBUG";
			 default -> "UNKNOWN";
    		};
    	}
}