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
    private boolean firstInit = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (firstInit)
            return;
        firstInit = true;

        for (Bean myBean : myBeans) {
            try {
                myBean.postConstruct();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}