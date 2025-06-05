package com.thitsaworks.dfsp_portal.component.usecase;

public class UseCaseContext {

    private static final InheritableThreadLocal<Object> detail = new InheritableThreadLocal<>();

    public static void set(Object detail) {

        UseCaseContext.detail.set(detail);

    }

    public static Object get() {

        return UseCaseContext.detail.get();

    }

}