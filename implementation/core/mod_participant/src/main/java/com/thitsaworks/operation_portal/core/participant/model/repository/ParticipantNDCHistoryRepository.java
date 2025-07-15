package com.thitsaworks.operation_portal.core.participant.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCHistoryId;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantNDCHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantNDCHistoryRepository extends JpaRepository<ParticipantNDCHistory, ParticipantNDCHistoryId>,
                                                         QuerydslPredicateExecutor<ParticipantNDCHistory> {
}
