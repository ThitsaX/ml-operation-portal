package com.thitsaworks.operation_portal.participant.domain.repository;

import com.thitsaworks.operation_portal.participant.domain.Participant;
import com.thitsaworks.operation_portal.participant.identity.ParticipantId;
import com.thitsaworks.operation_portal.participant.type.DfspCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, ParticipantId>,
        QuerydslPredicateExecutor<Participant> {
    public Optional<Participant> findByDfspCode(DfspCode dfspCode);

}
