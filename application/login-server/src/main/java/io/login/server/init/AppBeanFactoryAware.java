package io.login.server.init;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.stereotype.Component;

@Component
public class AppBeanFactoryAware implements BeanFactoryAware {

    @Value("${spring.beans.remove}")
    private String removeBeanDefinitions;
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        BeanDefinitionRegistry configurableBeanFactory =  (BeanDefinitionRegistry) beanFactory;
    }
}
