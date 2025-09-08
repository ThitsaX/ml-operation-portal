package com.thitsaworks.operation_portal.core.participant.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.Email;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.participant.data.UserData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.QUser;
import com.thitsaworks.operation_portal.core.participant.model.User;
import com.thitsaworks.operation_portal.core.participant.model.repository.UserRepository;
import com.thitsaworks.operation_portal.core.participant.query.UserQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CoreReadTransactional
public class UserJpaQueryHandler implements UserQuery {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserJpaQueryHandler.class);

    private final UserRepository userRepository;

    private final QUser user = QUser.user;

    @Override
    public List<UserData> getUsers() {

        BooleanExpression predicate = this.user.isNotNull();

        List<User> users = (List<User>) this.userRepository.findAll(predicate);

        return users.stream()
                    .map(UserData::new)
                    .toList();
    }

    @Override
    public List<UserData> getUsers(ParticipantId participantId) {

        BooleanExpression
            predicate =
                this.user.participant.participantId.eq(participantId)
                                                   .and(user.isDeleted.isFalse());

        List<User> users = (List<User>) this.userRepository.findAll(
            predicate);

        return users.stream()
                    .map(UserData::new)
                    .toList();

    }

    @Override
    public UserData get(UserId userId) throws ParticipantException {

        BooleanExpression predicate = this.user.userId.eq(userId);

        Optional<User> optionalUser = this.userRepository.findOne(predicate);

        if (optionalUser.isEmpty()) {

            throw new ParticipantException(ParticipantErrors.USER_NOT_FOUND);
        }

        return new UserData(optionalUser.get());
    }

    @Override
    public Optional<UserData> find(UserId userId) {

        BooleanExpression predicate = this.user.userId.eq(userId);

        Optional<User> optionalUser = this.userRepository.findOne(predicate);

        return optionalUser.map(UserData::new);

    }

    @Override
    public UserData get(Email email) throws ParticipantException {

        BooleanExpression predicate = this.user.email.eq(email);

        Optional<User> optionalUser = this.userRepository.findOne(predicate);

        if (optionalUser.isEmpty()) {

            throw new ParticipantException(ParticipantErrors.EMAIL_NOT_FOUND);
        }

        return new UserData(optionalUser.get());
    }

}
