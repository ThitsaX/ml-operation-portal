package com.thitsaworks.operation_portal.core.participant.model.repository;

import com.thitsaworks.operation_portal.core.participant.model.Contact;
import com.thitsaworks.component.common.identifier.ContactId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, ContactId>,
        QuerydslPredicateExecutor<Contact> {

}
