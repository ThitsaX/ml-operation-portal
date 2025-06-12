package com.thitsaworks.operation_portal.component.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContext implements ApplicationContextAware {

    private static final Logger LOG = LoggerFactory.getLogger(SpringContext.class);

    private static ApplicationContext applicationContext;

    public static <T> T getBean(Class<T> clazz) {

        return SpringContext.applicationContext.getBean(clazz);

    }

    public static <T> T getBean(Class<T> clazz, String qualifier) {

        return BeanFactoryAnnotationUtils.qualifiedBeanOfType(applicationContext.getAutowireCapableBeanFactory(), clazz,
                qualifier);

    }

    public static Object getBean(String beanName) {

        return SpringContext.applicationContext.getBean(beanName);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        LOG.info("applicationContext is set : {}", applicationContext);

        SpringContext.applicationContext = applicationContext;

    }

}