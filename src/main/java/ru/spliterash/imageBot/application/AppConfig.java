package ru.spliterash.imageBot.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.def.executors.DefaultCaseExecutor;
import ru.spliterash.imageBot.domain.pipeline.PipelineService;
import ru.spliterash.imageBot.messengers.domain.port.URLDownloader;
import ru.spliterash.imageBot.messengers.vk.VkMessenger;
import ru.spliterash.imageBot.pipelines.text.TextPipelineGenerator;

import javax.validation.Validator;
import java.util.concurrent.Executor;


@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") // У метода с интерфейсами свои недостатки.  :(
@Configuration
public class AppConfig {
    @Bean
    public CaseExecutor executor(
            Validator validator
    ) {
        return new DefaultCaseExecutor(validator);
    }

    @Bean
    public VkMessenger vkMessenger(
            TextPipelineGenerator generator,
            URLDownloader urlDownloader,
            PipelineService pipelineService,
            Executor executor,
            @Value("${vk.groupId}") int groupId,
            @Value("${vk.token}") String token
    ) {
        return new VkMessenger(
                generator,
                urlDownloader,
                pipelineService,
                executor,
                groupId,
                token
        );
    }
}
