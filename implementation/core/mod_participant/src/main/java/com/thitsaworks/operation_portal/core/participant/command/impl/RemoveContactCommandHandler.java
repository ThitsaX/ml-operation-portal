package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.RemoveContactCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoveContactCommandHandler implements RemoveContactCommand {

    private static final Logger LOG = LoggerFactory.getLogger(RemoveContactCommandHandler.class);

    private final ParticipantRepository participantRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantException {

        var
            participant = this.participantRepository.findById(input.participantId())
                                                    .orElseThrow(() -> new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND
                                                                                                    .format(input.participantId()
                                                                                                                 .getId())));

        var removed = participant.removeContact(input.contactId());

        this.participantRepository.saveAndFlush(participant);

        return new Output(removed, input.contactId());
    }

}
