package com.thitsaworks.operation_portal.component.misc.persistence.transactional;

import com.thitsaworks.operation_portal.component.misc.exception.IgnorableException;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(
    transactionManager = PersistenceQualifiers.Common.TRANSACTION_MANAGER,
    rollbackFor = Exception.class,
    noRollbackFor = IgnorableException.class,
    propagation = Propagation.REQUIRED,
    isolation = Isolation.READ_COMMITTED)
public @interface DfspWriteTransactional { }