package com.thitsaworks.operation_portal.core.participant.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.core.participant.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UserId>,
                                        QuerydslPredicateExecutor<User> {

    Optional<User> findByEmail(Email email);

    Optional<User> findByEmailAndIsDeleted(Email email, boolean isDeleted);

}
