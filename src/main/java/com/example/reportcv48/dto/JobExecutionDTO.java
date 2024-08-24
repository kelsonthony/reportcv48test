package com.example.reportcv48.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class JobExecutionDTO {
    private Long id;
    private String status;
    private String exitStatus;
    private Date startTime;
    private Date endTime;
    private List<StepExecutionDTO> stepExecutions;
    private Map<String, Object> jobParameters;

}


