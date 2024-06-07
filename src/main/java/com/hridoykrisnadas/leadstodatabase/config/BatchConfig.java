package com.hridoykrisnadas.leadstodatabase.config;

import com.hridoykrisnadas.leadstodatabase.model.Address;
import com.hridoykrisnadas.leadstodatabase.partition.AddressRowPartitioner;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class BatchConfig {

    private final AddressWriter addressWriter;

    @Bean
    @JobScope
    public FlatFileItemReader<Address> reader() {
        FlatFileItemReader<Address> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new ClassPathResource("csv/adressen_csv.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(getLineMapper());
        return itemReader;
    }

    private LineMapper<Address> getLineMapper() {
        DefaultLineMapper<Address> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter("|");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "straat", "huisnummer", "toevoeging", "postcode", "gemeente", "woonplaats", "provincie", "latitude", "longitude", "created_at", "updated_at");
        BeanWrapperFieldSetMapper<Address> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Address.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }

    @Bean
    @StepScope
    public AddressItemProcessor processor() {
        return new AddressItemProcessor();
    }

    @Bean
    public AddressRowPartitioner partitioner() {
        return new AddressRowPartitioner();
    }


    @Bean
    public Step childStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("csv-step", jobRepository)
                .<Address, Address>chunk(50000, platformTransactionManager)
                .reader(reader())
                .processor(processor())
                .writer(addressWriter)
                .build();
    }

    @Bean
    public Job runjob(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new JobBuilder("ImportAddress", jobRepository)
                .flow(childStep(jobRepository, platformTransactionManager))
                .end().build();
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
    public TaskExecutorPartitionHandler partitionHandler(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        TaskExecutorPartitionHandler taskExecutorPartitionHandler = new TaskExecutorPartitionHandler();
        taskExecutorPartitionHandler.setGridSize(4);
        taskExecutorPartitionHandler.setStep(childStep(jobRepository, platformTransactionManager));
        taskExecutorPartitionHandler.setTaskExecutor(taskExecutor());
        return taskExecutorPartitionHandler;
    }

    @Bean
    public Step parentStep(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("parentStep", jobRepository)
                .partitioner("childStep", partitioner())
                .partitionHandler(partitionHandler(jobRepository, platformTransactionManager))
                .build();
    }
}
