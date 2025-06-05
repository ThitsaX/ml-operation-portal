package com.thitsaworks.dfsp_portal.participant.domain.repository;

import com.thitsaworks.dfsp_portal.component.type.Email;
import com.thitsaworks.dfsp_portal.participant.domain.ParticipantUser;
import com.thitsaworks.dfsp_portal.participant.identity.ParticipantUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ParticipantUserRepository extends JpaRepository<ParticipantUser, ParticipantUserId>,
        QuerydslPredicateExecutor<ParticipantUser> {
    public Optional<ParticipantUser> findByEmail(Email email);

    public Optional<ParticipantUser> findByEmailAndIsDeleted(Email email,boolean isDeleted);

}
