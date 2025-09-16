package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.CreateGreetingMessageCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.GreetingMessage;
import com.thitsaworks.operation_portal.core.participant.model.repository.GreetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateGreetingMessageCommandHandler implements CreateGreetingMessageCommand {

    private final GreetingRepository greetingRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantException {
        Optional<GreetingMessage> optionalGreetingMessage = this.greetingRepository.findOne(
            GreetingRepository.Filters.findByGreetingTitle(input.greetingTitle()));

        if (optionalGreetingMessage.isPresent()) {

            throw new ParticipantException(ParticipantErrors.GREETING_MESSAGE_ALREADY_REGISTERED.format(input.greetingTitle()));
        }

        var greeting = new GreetingMessage(input.greetingTitle(),
                                           input.greetingDetail(),
                                           input.greetingDate());

        this.greetingRepository.save(greeting);

        return  new Output(true);


    }

}
