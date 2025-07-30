package com.thitsaworks.operation_portal.core.test_iam.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.BlockedActionId;
import com.thitsaworks.operation_portal.core.test_iam.model.BlockedAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BlockedActionRepository extends JpaRepository<BlockedAction, BlockedActionId>,
                                                 QuerydslPredicateExecutor<BlockedAction> {

    class Filters{
    }
}
