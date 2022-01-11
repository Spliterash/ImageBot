package ru.spliterash.imageBot.application.port;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.spliterash.imageBot.domain.port.ContextProvider;

@Component
@RequiredArgsConstructor
public class SpringContextProviderImpl implements ContextProvider {
    private final ApplicationContext context;

    @Override
    public <T> T get(Class<T> clazz) {
        return context.getBean(clazz);
    }
}
