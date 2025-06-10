package com.thitsaworks.operation_portal.participant.domain.repository;

import com.thitsaworks.operation_portal.participant.domain.Contact;
import com.thitsaworks.operation_portal.participant.identity.ContactId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, ContactId>,
        QuerydslPredicateExecutor<Contact> {

}
