package com.thitsaworks.operation_portal.usecase.util.action;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class UseCaseRegistrationBeanPostProcessor implements BeanPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(UseCaseRegistrationBeanPostProcessor.class);

    private static final String TARGET_PACKAGE = "com.thitsaworks.operation_portal.usecase.operation_portal.impl";

    private final ActionAuthorizationManager actionAuthorizationManager;

    @Autowired
    public UseCaseRegistrationBeanPostProcessor(ActionAuthorizationManager actionAuthorizationManager) {

        this.actionAuthorizationManager = actionAuthorizationManager;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, @NotNull String beanName) {

        Class<?> clazz = bean.getClass();

        if (clazz.getPackageName()
                 .startsWith(TARGET_PACKAGE)) {

            String simpleName = clazz.getSimpleName();
            String actionName = simpleName.replaceFirst("(Handler|UseCase)$", "");

            try {

                this.actionAuthorizationManager.registerAction(actionName,
                                                               "OPERATION_PORTAL",
                                                               "Automatically registered action for use case: " +
                                                                   simpleName);

            } catch (Exception e) {
                LOG.error("Failed to register use case action [{}]: {}", actionName, e.getMessage());
            }
        }

        return bean;
    }

}
