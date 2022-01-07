package ru.spliterash.imageBot.application.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.spliterash.imageBot.domain.def.bean.Bean;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ApplicationContextLoadListener implements ApplicationListener<ContextRefreshedEvent> {
    private final Set<Bean> myBeans;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        myBeans.forEach(Bean::postConstruct);
    }
}