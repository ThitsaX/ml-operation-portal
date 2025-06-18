package com.thitsaworks.operation_portal.core.participant.model.repository;

import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.DfspCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, ParticipantId>,
        QuerydslPredicateExecutor<Participant> {
    public Optional<Participant> findByDfspCode(DfspCode dfspCode);

}
