package com.thitsaworks.operation_portal.core.participant.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantNDCId;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreReadTransactional;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantNDCData;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNDCException;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantNDC;
import com.thitsaworks.operation_portal.core.participant.model.QParticipantNDC;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantNDCRepository;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantNDCQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@CoreReadTransactional
public class ParticipantNDCJpaQueryHandler implements ParticipantNDCQuery {

    private static final Logger LOG = LoggerFactory.getLogger(ParticipantNDCJpaQueryHandler.class);

    private final ParticipantNDCRepository participantNDCRepository;

    private final QParticipantNDC participantNDC = QParticipantNDC.participantNDC;

    @Override
    public ParticipantNDCData get(ParticipantNDCId participantNDCId) throws ParticipantNDCException {

        BooleanExpression predicate = this.participantNDC.participantNDCId.eq(participantNDCId);

        Optional<ParticipantNDC> optionalParticipantNDC = this.participantNDCRepository.findOne(predicate);

        if (optionalParticipantNDC.isEmpty()) {

            throw new ParticipantNDCException(ParticipantErrors.PARTICIPANT_NDC_NOT_FOUND.format(participantNDCId.getId()));
        }

        return new ParticipantNDCData(optionalParticipantNDC.get());
    }

    @Override
    public Optional<ParticipantNDCData> get(String dfspCode, String currency) {

        BooleanExpression predicate = this.participantNDC.dfspCode.eq(dfspCode).and(this.participantNDC.currency.eq(
                currency));

        Optional<ParticipantNDC> optionalParticipantNDC = this.participantNDCRepository.findOne(predicate);

        return optionalParticipantNDC.map(ParticipantNDCData::new);
    }

}
