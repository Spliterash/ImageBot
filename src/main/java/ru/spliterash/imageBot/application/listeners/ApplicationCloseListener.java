package ru.spliterash.imageBot.application.listeners;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;
import ru.spliterash.imageBot.domain.def.bean.Bean;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class ApplicationCloseListener implements ApplicationListener<ContextClosedEvent> {
    private final Set<Bean> myBeans;

    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        for (Bean myBean : myBeans) {
            try {
                myBean.preDestroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
