package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.CreateContactHistoryCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.ContactHistory;
import com.thitsaworks.operation_portal.core.participant.model.repository.ContactHistoryRepository;
import com.thitsaworks.operation_portal.core.participant.model.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateContactHistoryCommandHandler implements CreateContactHistoryCommand {

    private final ContactHistoryRepository contactHistoryRepository;

    private final ContactRepository contactRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantException {

        var contact= this.contactRepository.findById(input.contactId())
                                                  .orElseThrow(()-> new ParticipantException(ParticipantErrors.CONTACT_NOT_FOUND.defaultMessage(
                                                          "System cannot find the contact with provided ID [" + input.contactId().getId() + "].")));

        var history =new ContactHistory(contact.getContactId(),
                                        input.participantId(),
                                        contact.getName(),
                                        contact.getPosition(),
                                        contact.getEmail(),
                                        contact.getMobile(),
                                        contact.getContactType());

        this.contactHistoryRepository.save(history);

        return new Output(history.getContactHistoryId());
    }

}
