package com.thitsaworks.dfsp_portal.participant.domain.repository;

import com.thitsaworks.dfsp_portal.participant.domain.LiquidityProfile;
import com.thitsaworks.dfsp_portal.participant.identity.LiquidityProfileId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LiquidityProfileRepository extends JpaRepository<LiquidityProfile, LiquidityProfileId>,
        QuerydslPredicateExecutor<LiquidityProfile> {

}
