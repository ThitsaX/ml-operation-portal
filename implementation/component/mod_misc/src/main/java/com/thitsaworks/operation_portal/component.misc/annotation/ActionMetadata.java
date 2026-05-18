package com.thitsaworks.operation_portal.component.misc.annotation;

import com.thitsaworks.operation_portal.component.misc.util.ActionCategory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionMetadata {

    ActionCategory category();

    String description() default "";

    boolean isMandatory() default false;

}