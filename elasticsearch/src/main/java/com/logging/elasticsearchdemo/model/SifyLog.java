package com.logging.elasticsearchdemo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.OptBoolean;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;
import java.util.Map;

@Document(indexName = "sify-logs")
public class SifyLog {
	
    @Id
    private String id;

    private String program;
    
    @Field(name = "syslog_pri")
    private Integer syslogPri;
    
    private String log_message;
    private String type;
    private String message;

    @Field(type = FieldType.Text)
    private String pid;

    private Host host;  // Maps "host.ip"

    @Field(type = FieldType.Date, name = "@timestamp")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss[.SSS]X", lenient = OptBoolean.TRUE)
    private Instant timestamp;  // Changed from String → Instant
    private Map<String, Object> event;
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }

    public Integer getSyslogPri() { return syslogPri; }
    public void setSyslogPri(Integer syslogPri) { this.syslogPri = syslogPri; }

    public String getLog_message() { return log_message; }
    public void setLog_message(String log_message) { this.log_message = log_message; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPid() { return pid; }
    public void setPid(String pid) { this.pid = pid; }

    public Host getHost() { return host; }
    public void setHost(Host host) { this.host = host; }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public Map<String, Object> getEvent() { return event; }
    public void setEvent(Map<String, Object> event) { this.event = event; }
}
