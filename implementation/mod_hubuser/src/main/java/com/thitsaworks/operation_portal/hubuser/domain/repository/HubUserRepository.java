package com.thitsaworks.operation_portal.hubuser.domain.repository;

import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.hubuser.domain.HubUser;
import com.thitsaworks.operation_portal.hubuser.identity.HubUserId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface HubUserRepository extends JpaRepository<HubUser, HubUserId>,
        QuerydslPredicateExecutor<HubUser> {

    public Optional<HubUser> findByEmail(Email email);

}
