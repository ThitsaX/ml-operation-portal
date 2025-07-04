package com.thitsaworks.operation_portal.core.hubuser.query.impl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.HubUserId;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.hubuser.data.HubUserData;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserErrors;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserException;
import com.thitsaworks.operation_portal.core.hubuser.model.HubUser;
import com.thitsaworks.operation_portal.core.hubuser.model.QHubUser;
import com.thitsaworks.operation_portal.core.hubuser.model.repository.HubUserRepository;
import com.thitsaworks.operation_portal.core.hubuser.query.HubUserQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CoreReadTransactional
public class HubUserQueryHandler implements HubUserQuery {

    private final HubUserRepository hubUserRepository;

    private final QHubUser hubUser = QHubUser.hubUser;

    @Override
    public List<HubUserData> getHubUsers() {

        BooleanExpression predicate = this.hubUser.isNotNull().and(hubUser.isDeleted.isFalse());

        List<HubUser> hubUsers = (List<HubUser>) this.hubUserRepository.findAll(predicate);

        return hubUsers.stream().map(HubUserData::new).toList();
    }

    @Override
    public HubUserData get(HubUserId hubUserId) throws HubUserException {

        BooleanExpression predicate = this.hubUser.userId.eq(hubUserId);

        Optional<HubUser> optionalHubUser = this.hubUserRepository.findOne(predicate);

        if (optionalHubUser.isEmpty()) {

            throw new HubUserException(HubUserErrors.USER_NOT_FOUND);
        }

        return new HubUserData(optionalHubUser.get());
    }

}
