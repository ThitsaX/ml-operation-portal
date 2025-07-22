package com.thitsaworks.operation_portal.core.participant.model.repository;

import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantUser;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ParticipantUserRepository extends JpaRepository<ParticipantUser, ParticipantUserId>,
        QuerydslPredicateExecutor<ParticipantUser> {
    public Optional<ParticipantUser> findByEmail(Email email);

    public Optional<ParticipantUser> findByEmailAndIsDeleted(Email email,boolean isDeleted);

}
