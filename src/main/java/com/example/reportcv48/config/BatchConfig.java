package com.example.reportcv48.config;


import com.example.reportcv48.entity.Customer;
import com.example.reportcv48.listener.StepSkipListener;
import com.example.reportcv48.partition.ColumnRangePartitioner;
import com.example.reportcv48.writer.CustomerItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CustomerItemWriter customerWriter;
    private final JobExecutionListener jobExecutionListener;
    private final SkipPolicy skipPolicy;
    private final ItemProcessor<Customer, Customer> processor;
    private final FlatFileItemReader<Customer> reader;


    public BatchConfig(JobBuilderFactory jobBuilderFactory,
                       StepBuilderFactory stepBuilderFactory,
                       CustomerItemWriter customerWriter,
                       @Qualifier("customJobExecutionListener")
                             JobExecutionListener jobExecutionListener,
                       SkipPolicy skipPolicy, ItemProcessor<Customer, Customer> processor,
                       FlatFileItemReader<Customer> reader) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.customerWriter = customerWriter;
        this.jobExecutionListener = jobExecutionListener;
        this.skipPolicy = skipPolicy;
        this.processor = processor;
        this.reader = reader;
    }


    @Bean
    public ColumnRangePartitioner partitioner() {
        return new ColumnRangePartitioner();
    }

    @Bean
    public PartitionHandler partitionHandler() {
        TaskExecutorPartitionHandler taskExecutorPartitionHandler = new TaskExecutorPartitionHandler();
        taskExecutorPartitionHandler.setGridSize(2);
        taskExecutorPartitionHandler.setTaskExecutor(taskExecutor());
        taskExecutorPartitionHandler.setStep(slaveStep());

        return taskExecutorPartitionHandler;
    }

    @Bean
    public Step slaveStep() {
        return stepBuilderFactory.get("slaveStep").<Customer, Customer>chunk(5)
                .reader(reader)
                .processor(processor)
                .writer(customerWriter)
                .faultTolerant()
                //.listener(jobExecutionListener)
                .skipPolicy(skipPolicy)
                .build();
    }

    @Bean
    public Step masterStep() {
        return stepBuilderFactory.get("masterStep")
                .partitioner(slaveStep().getName(), partitioner())
                .partitionHandler(partitionHandler())
                .build();
    }

    @Bean
    public Job runJob() {
        return jobBuilderFactory.get("importCustomers")
                .flow(masterStep()).end().build();

    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setMaxPoolSize(4);
        threadPoolTaskExecutor.setCorePoolSize(4);
        threadPoolTaskExecutor.setQueueCapacity(4);

        return threadPoolTaskExecutor;
    }


    @Bean
    public SkipListener skipListener() {
        return new StepSkipListener();
    }

}
