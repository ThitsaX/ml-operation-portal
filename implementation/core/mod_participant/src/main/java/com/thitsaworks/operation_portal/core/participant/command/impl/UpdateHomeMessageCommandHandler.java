package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.UpdateGreetingCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.GreetingMessage;
import com.thitsaworks.operation_portal.core.participant.model.repository.GreetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateHomeMessageCommandHandler implements UpdateGreetingCommand {

    private final GreetingRepository greetingRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantException {

        var greeting = this.greetingRepository.findById(input.greetingId())
                                              .orElseThrow(() -> new ParticipantException(
                                                      ParticipantErrors.GREETING_MESSAGE_NOT_FOUND.format(input.greetingId().getId().toString())));

        Optional<GreetingMessage> optionalGreetingMessage = this.greetingRepository.findOne(
            GreetingRepository.Filters.findByGreetingTitle(input.greetingTitle()));

        if (optionalGreetingMessage.isPresent() &&
                !optionalGreetingMessage.get().getGreetingId().equals(greeting.getGreetingId())) {
            throw new ParticipantException(ParticipantErrors.GREETING_MESSAGE_ALREADY_REGISTERED.format(input.greetingTitle()));
        }

        greeting.greetingTitle(input.greetingTitle());
        greeting.greetingDetail(input.greetingDetail());
        greeting.isDeleted(input.isDeleted());
        greeting.greetingDate(input.greetDate());



        this.greetingRepository.save(greeting);

        return new Output(greeting.getGreetingId());

    }


    }

