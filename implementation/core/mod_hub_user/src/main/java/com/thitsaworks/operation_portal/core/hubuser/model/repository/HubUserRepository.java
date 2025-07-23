package com.thitsaworks.operation_portal.core.hubuser.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.core.hubuser.model.HubUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface HubUserRepository extends JpaRepository<HubUser, HubUserId>,
        QuerydslPredicateExecutor<HubUser> {

    public Optional<HubUser> findByEmail(Email email);

}
