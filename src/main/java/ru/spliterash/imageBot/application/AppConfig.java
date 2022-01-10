package ru.spliterash.imageBot.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.spliterash.imageBot.domain.def.CaseExecutor;
import ru.spliterash.imageBot.domain.def.executors.DefaultCaseExecutor;
import ru.spliterash.imageBot.domain.pipeline.PipelineService;
import ru.spliterash.imageBot.domain.utils.ThreadUtils;
import ru.spliterash.imageBot.messengers.domain.commands.BotCommand;
import ru.spliterash.imageBot.messengers.domain.port.URLDownloader;
import ru.spliterash.imageBot.messengers.vk.VkMessenger;
import ru.spliterash.imageBot.pipelines.text.CLIPipelineGenerator;

import javax.validation.Validator;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") // У метода с интерфейсами свои недостатки.  :(
@Configuration
public class AppConfig {
    @Bean
    public CaseExecutor caseExecutor(
            Validator validator
    ) {
        return new DefaultCaseExecutor(validator);
    }

    @Bean
    public Executor threadExecutor() {
        return Executors.newCachedThreadPool();
    }

    @Bean
    public VkMessenger vkMessenger(
            CLIPipelineGenerator generator,
            URLDownloader urlDownloader,
            PipelineService pipelineService,
            ThreadUtils threadUtils,
            Set<BotCommand> commandList,

            @Value("${vk.groupId}") int groupId,
            @Value("${vk.token}") String token
    ) {
        return new VkMessenger(
                generator,
                urlDownloader,
                pipelineService,
                threadUtils,
                commandList,
                groupId,
                token
        );
    }
}
