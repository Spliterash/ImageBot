package ru.spliterash.imageBot.application.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.stereotype.Component;
import ru.spliterash.imageBot.domain.def.bean.Bean;

@Component
public class BeanLifecycleProcessor implements DestructionAwareBeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object object, String beanName) throws BeansException {
        if (object instanceof Bean) {
            try {
                ((Bean) object).postConstruct();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return object;
    }

    @Override
    public void postProcessBeforeDestruction(Object object, String beanName) throws BeansException {
        if (object instanceof Bean) {
            try {
                ((Bean) object).preDestroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
