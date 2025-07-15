package com.thitsaworks.operation_portal.core.participant.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantNDC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantNDCRepository extends JpaRepository<ParticipantNDC, ParticipantNDCId>,
                                                  QuerydslPredicateExecutor<ParticipantNDC> {
}
