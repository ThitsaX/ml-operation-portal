package com.thitsaworks.operation_portal.core.participant.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.type.ParticipantName;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.QParticipant;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CoreReadTransactional
public class ParticipantJpaQueryHandler implements ParticipantQuery {


    private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantJpaQueryHandler.class);

    private final ParticipantRepository participantRepository;

    private final QParticipant participant = QParticipant.participant;

    @Override
    public List<ParticipantData> getParticipants() {

        BooleanExpression predicate = this.participant.isNotNull();

        List<Participant> participants = (List<Participant>) this.participantRepository.findAll(predicate);

        return participants.stream().map(ParticipantData::new).toList();

    }

    @Override
    public ParticipantData get(ParticipantId participantId) throws ParticipantException {

        BooleanExpression predicate = this.participant.participantId.eq(participantId);

        Optional<Participant> optionalParticipant = this.participantRepository.findOne(predicate);

        if (optionalParticipant.isEmpty()) {

            throw new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND
                    .format(participantId.getId().toString()));
        }

        return new ParticipantData(optionalParticipant.get());
    }

    @Override
    public List<ParticipantData> getOtherParticipants(ParticipantId participantId) {

        BooleanExpression predicate = this.participant.participantId.ne(participantId);

        List<Participant> participants = (List<Participant>) this.participantRepository.findAll(predicate);

        return participants.stream().map(ParticipantData::new).toList();

    }

    @Override
    public Optional<ParticipantData> get(String participantName) {

        BooleanExpression predicate = this.participant.participantName.eq(new ParticipantName(participantName));

        Optional<Participant> optionalParticipant = this.participantRepository.findOne(predicate);

        if(optionalParticipant.isEmpty())
        {
            return Optional.empty();
        }

        return Optional.of(new ParticipantData(optionalParticipant.get()));
    }

}
