package ru.spliterash.imageBot.application;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.Configuration;
import ru.spliterash.imageBot.domain.def.bean.Bean;
import ru.spliterash.imageBot.domain.def.bean.NoAutoCreate;

import java.lang.reflect.Modifier;
import java.util.Set;

@Configuration
public class AutoBeanConfig implements BeanDefinitionRegistryPostProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(AutoBeanConfig.class);

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        Reflections reflections = new Reflections("ru.spliterash");
        Set<Class<? extends Bean>> classes = reflections.getSubTypesOf(Bean.class);

        classes
                .stream()
                .filter(c -> !c.isAnnotationPresent(NoAutoCreate.class))
                .filter(c -> !c.isInterface())
                .filter(c -> !Modifier.isAbstract(c.getModifiers()))
                .forEach(clazz -> {
                    try {
                        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                                .genericBeanDefinition(Class.forName(clazz.getName())).setLazyInit(false);
                        registry.registerBeanDefinition(clazz.getName(),
                                builder.getBeanDefinition());
                    } catch (ClassNotFoundException e) {
                        throw new IllegalArgumentException(e);
                    }
                    LOG.info("Creating - {} bean", clazz.getName());
                });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }
}
