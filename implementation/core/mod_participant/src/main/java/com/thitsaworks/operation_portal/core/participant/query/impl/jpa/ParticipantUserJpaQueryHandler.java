package com.thitsaworks.operation_portal.core.participant.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.component.common.identifier.ParticipantId;
import com.thitsaworks.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.component.type.Email;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantUserData;
import com.thitsaworks.operation_portal.core.participant.exception.EmailNotFoundException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantUserNotFoundException;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantUser;
import com.thitsaworks.operation_portal.core.participant.model.QParticipantUser;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantUserRepository;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantUserQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CoreReadTransactional
public class ParticipantUserJpaQueryHandler implements ParticipantUserQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantUserJpaQueryHandler.class);

    private final ParticipantUserRepository participantUserRepository;

    private final QParticipantUser participantUser = QParticipantUser.participantUser;

    @Override
    public List<ParticipantUserData> getParticipantUsers(ParticipantId participantId) {

        BooleanExpression predicate = this.participantUser.participant.participantId.eq(participantId).and(participantUser.isDeleted.isFalse());

        List<ParticipantUser> participantUsers = (List<ParticipantUser>) this.participantUserRepository.findAll(
                predicate);

        return participantUsers.stream().map(ParticipantUserData::new).toList();

    }

    @Override
    public ParticipantUserData get(ParticipantUserId participantUserId) throws ParticipantUserNotFoundException {

        BooleanExpression predicate = this.participantUser.participantUserId.eq(participantUserId);

        Optional<ParticipantUser> optionalParticipantUser = this.participantUserRepository.findOne(predicate);

        if (optionalParticipantUser.isEmpty()) {

            throw new ParticipantUserNotFoundException(participantUserId.getId().toString());
        }

        return new ParticipantUserData(optionalParticipantUser.get());
    }

    @Override
    public ParticipantUserData get(Email email) throws EmailNotFoundException {

        BooleanExpression predicate = this.participantUser.email.eq(email);

        Optional<ParticipantUser> optionalParticipantUser = this.participantUserRepository.findOne(predicate);

        if (optionalParticipantUser.isEmpty()) {

            throw new EmailNotFoundException(email.toString());
        }

        return new ParticipantUserData(optionalParticipantUser.get());
    }

}
