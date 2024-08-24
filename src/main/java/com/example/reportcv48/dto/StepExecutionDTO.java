package com.example.reportcv48.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

import org.springframework.batch.core.JobExecution;

@Data
public class StepExecutionDTO {
    private Long id;
    private String name;
    private String status;
    private String exitStatus;
    private int readCount;
    private int filterCount;
    private int writeCount;
    private int readSkipCount;
    private int writeSkipCount;
    private int processSkipCount;
    private int commitCount;
    private int rollbackCount;

    @JsonIgnore
    private JobExecution jobExecution;

    public StepExecutionDTO() {
    }

    public StepExecutionDTO(Long id, String name, String status, String exitStatus, int readCount, int filterCount, int writeCount, int readSkipCount, int writeSkipCount, int processSkipCount, int commitCount, int rollbackCount) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.exitStatus = exitStatus;
        this.readCount = readCount;
        this.filterCount = filterCount;
        this.writeCount = writeCount;
        this.readSkipCount = readSkipCount;
        this.writeSkipCount = writeSkipCount;
        this.processSkipCount = processSkipCount;
        this.commitCount = commitCount;
        this.rollbackCount = rollbackCount;
    }
}