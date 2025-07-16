package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.SaveContactCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SaveContactCommandHandler implements SaveContactCommand {

    private static final Logger LOG = LoggerFactory.getLogger(SaveContactCommandHandler.class);

    private final ParticipantRepository participantRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantException {

        var
            participant =
            this.participantRepository.findById(input.participantId())
                                      .orElseThrow(() -> new ParticipantException(ParticipantErrors.PARTICIPANT_NOT_FOUND));

        var contact = participant.saveContact(input.contactId(),
                                              input.name(),
                                              input.title(),
                                              input.email(),
                                              input.mobile(),
                                              input.contactType());

        this.participantRepository.saveAndFlush(participant);

        return new SaveContactCommand.Output(true, contact.getContactId());
    }

}
