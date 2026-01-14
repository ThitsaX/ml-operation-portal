package com.thitsaworks.operation_portal.usecase.util.action;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UseCaseRegistrationBeanPostProcessor implements BeanPostProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(UseCaseRegistrationBeanPostProcessor.class);

    private static final List<String> TARGET_PACKAGE_LIST = new ArrayList<>(List.of(
            "com.thitsaworks.operation_portal.usecase.operation_portal.impl",
            "com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.jobs"));

    private final ObjectProvider<ActionAuthorizationManager> actionAuthorizationManager;

    @Autowired
    public UseCaseRegistrationBeanPostProcessor(ObjectProvider<ActionAuthorizationManager> actionAuthorizationManager) {

        this.actionAuthorizationManager = actionAuthorizationManager;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, @NotNull String beanName) {

        Class<?> clazz = bean.getClass();// Handle CGLIB proxies
        if (clazz.getName().contains("$$")) {
            clazz = clazz.getSuperclass();
        }

        Class<?> finalClazz = clazz;
        if (TARGET_PACKAGE_LIST.stream()
                               .anyMatch(pkg -> finalClazz.getPackageName().startsWith(pkg))) {

            String simpleName = clazz.getSimpleName();
            String actionName = simpleName.replaceFirst("(Handler|UseCase)$", "");

            try {
                ActionAuthorizationManager manager = this.actionAuthorizationManager.getIfAvailable();
                if (manager != null) {
                    manager.registerAction(
                        actionName,
                        "OPERATION_PORTAL",
                        "Automatically registered action for use case: " + simpleName);
                } else {
                    LOG.warn("ActionAuthorizationManager not available when registering action: {}", actionName);
                }

            } catch (Exception e) {
                LOG.error("Failed to register use case action [{}]: {}", actionName, e.getMessage(), e);
            }
        }

        return bean;
    }

}
