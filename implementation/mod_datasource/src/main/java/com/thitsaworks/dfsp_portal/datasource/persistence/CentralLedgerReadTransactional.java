package com.thitsaworks.dfsp_portal.datasource.persistence;

import com.thitsaworks.dfsp_portal.component.exception.IgnorableException;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Transactional(
        transactionManager = "centralLedgerReadTransactionManager",
        rollbackFor = Exception.class,
        noRollbackFor = IgnorableException.class)
public @interface CentralLedgerReadTransactional {

}
