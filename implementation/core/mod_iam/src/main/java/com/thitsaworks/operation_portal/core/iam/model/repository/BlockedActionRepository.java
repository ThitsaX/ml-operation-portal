package com.thitsaworks.operation_portal.core.iam.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.BlockedActionId;
import com.thitsaworks.operation_portal.core.iam.model.BlockedAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface BlockedActionRepository extends JpaRepository<BlockedAction, BlockedActionId>,
                                                 QuerydslPredicateExecutor<BlockedAction> {

    class Filters{
    }
}
