package com.thitsaworks.operation_portal.dfsp_portal.participant.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.DfspReadTransactional;
import com.thitsaworks.operation_portal.dfsp_portal.participant.domain.Participant;
import com.thitsaworks.operation_portal.dfsp_portal.participant.domain.QParticipant;
import com.thitsaworks.operation_portal.dfsp_portal.participant.domain.repository.ParticipantRepository;
import com.thitsaworks.operation_portal.dfsp_portal.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.dfsp_portal.participant.query.data.ParticipantData;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
@RequiredArgsConstructor
@DfspReadTransactional
public class ParticipantJpaQueryHandler implements ParticipantQuery {


    private static final Logger LOGGER = LoggerFactory.getLogger(ParticipantJpaQueryHandler.class);

    private final ParticipantRepository participantRepository;

    private final QParticipant participant = QParticipant.participant;

    @Override
    public List<ParticipantData> getParticipants() {

        BooleanExpression predicate = this.participant.isNotNull();

        List<Participant> participants = (List<Participant>) this.participantRepository.findAll(predicate);

        return participants
                .stream()
                .map(inst -> new ParticipantData(inst))
                .toList();

    }

}
