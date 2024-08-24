package com.example.reportcv48.listener;

import com.example.reportcv48.dto.JobExecutionDTO;
import com.example.reportcv48.dto.StepExecutionDTO;
import lombok.Getter;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@Component
public class CustomJobExecutionListener implements JobExecutionListener {

    private Map<Long, JobExecutionDTO> jobResults = new HashMap<>();

    @Override
    public void beforeJob(JobExecution jobExecution) {
        // Lógica de inicialização antes do trabalho começar, se necessário
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        // Limpa resultados anteriores, se necessário
        jobResults.clear();

        // Converte JobExecution para JobExecutionDTO
        JobExecutionDTO jobExecutionDTO = convertToJobExecutionDTO(jobExecution);

        // Armazena os resultados usando o ID do JobExecution
        jobResults.put(jobExecution.getId(), jobExecutionDTO);

        // Imprime os resultados para fins de depuração
        System.out.println("Job completed. Results: " + jobResults);
    }

    // Método para acessar resultados fora do listener
    public JobExecutionDTO getJobResult(Long executionId) {
        return jobResults.get(executionId);
    }

    private JobExecutionDTO convertToJobExecutionDTO(JobExecution jobExecution) {
        List<StepExecutionDTO> stepExecutionDTOs = jobExecution.getStepExecutions().stream()
                .map(this::convertToStepExecutionDTO)
                .collect(Collectors.toList());

        // Convertendo os parâmetros de JobParameter para Object
        Map<String, Object> jobParameters = jobExecution.getJobParameters().getParameters().entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getValue() // Aqui, extrai o valor de JobParameter como Object
                ));

        JobExecutionDTO jobExecutionDTO = new JobExecutionDTO();
        jobExecutionDTO.setId(jobExecution.getId());
        jobExecutionDTO.setStatus(jobExecution.getStatus().toString());
        jobExecutionDTO.setExitStatus(jobExecution.getExitStatus().getExitCode());
        jobExecutionDTO.setStartTime(jobExecution.getStartTime());
        jobExecutionDTO.setEndTime(jobExecution.getEndTime());
        jobExecutionDTO.setJobParameters(jobParameters); // Agora usando o Map convertido
        jobExecutionDTO.setStepExecutions(stepExecutionDTOs);

        return jobExecutionDTO;
    }


    private StepExecutionDTO convertToStepExecutionDTO(StepExecution stepExecution) {
        return new StepExecutionDTO(
                stepExecution.getId(),
                stepExecution.getStepName(),
                stepExecution.getStatus().toString(),
                stepExecution.getExitStatus().getExitCode(),
                stepExecution.getReadCount(),
                stepExecution.getFilterCount(),
                stepExecution.getWriteCount(),
                stepExecution.getReadSkipCount(),
                stepExecution.getWriteSkipCount(),
                stepExecution.getProcessSkipCount(),
                stepExecution.getCommitCount(),
                stepExecution.getRollbackCount()
        );
    }
}
