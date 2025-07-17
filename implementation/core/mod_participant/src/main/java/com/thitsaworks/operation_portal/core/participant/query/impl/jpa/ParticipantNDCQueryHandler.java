package com.thitsaworks.operation_portal.core.participant.query.impl.jpa;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantNDCData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNDCException;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantNDCRepository;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantNDCQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@CoreReadTransactional
public class ParticipantNDCQueryHandler implements ParticipantNDCQuery {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantNDCQueryHandler.class);

    private final ParticipantNDCRepository participantNDCRepository;

    @Override
    public ParticipantNDCData get(ParticipantNDCId participantNDCId) throws ParticipantNDCException {

//        BooleanExpression predicate = this.participant.participantId.eq(participantId);
//
//        Optional<ParticipantNDC> optionalParticipantNDC = this.participantNDCRepository.findOne(predicate);
//
//        if (optionalParticipantNDC.isEmpty()) {
//
//            throw new ParticipantNDCException(ParticipantErrors.PARTICIPANT_NOT_FOUND);
//        }
//
//        return new ParticipantNDCData(optionalParticipant.get());
        return null;
    }

}
