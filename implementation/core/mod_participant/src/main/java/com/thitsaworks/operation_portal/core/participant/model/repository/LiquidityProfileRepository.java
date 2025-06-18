package com.thitsaworks.operation_portal.core.participant.model.repository;

import com.thitsaworks.operation_portal.core.participant.model.LiquidityProfile;
import com.thitsaworks.operation_portal.component.common.identifier.LiquidityProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LiquidityProfileRepository extends JpaRepository<LiquidityProfile, LiquidityProfileId>,
        QuerydslPredicateExecutor<LiquidityProfile> {

}
