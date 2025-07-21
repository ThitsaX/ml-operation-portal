package com.thitsaworks.operation_portal.core.participant.model.repository;

import com.thitsaworks.operation_portal.component.common.identifier.ContactHistoryId;
import com.thitsaworks.operation_portal.core.participant.model.ContactHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactHistoryRepository extends JpaRepository<ContactHistory , ContactHistoryId>,
                                                  QuerydslPredicateExecutor<ContactHistory> {

}
