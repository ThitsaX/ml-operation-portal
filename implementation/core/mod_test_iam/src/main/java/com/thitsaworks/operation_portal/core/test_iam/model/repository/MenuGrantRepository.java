package com.thitsaworks.operation_portal.core.test_iam.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.MenuId;
import com.thitsaworks.operation_portal.core.test_iam.model.MenuGrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuGrantRepository
    extends JpaRepository<MenuGrant, MenuId>, QuerydslPredicateExecutor<MenuGrant> {

    class Filters {}

}
