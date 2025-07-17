package com.thitsaworks.operation_portal.core.hubuser.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.hubuser.command.UpdateGreetingCommand;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserErrors;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserException;
import com.thitsaworks.operation_portal.core.hubuser.model.GreetingMessage;
import com.thitsaworks.operation_portal.core.hubuser.model.repository.GreetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UpdateHomeMessageCommandHandler implements UpdateGreetingCommand {

    private final GreetingRepository greetingRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws HubUserException {

        var greeting = this.greetingRepository.findById(input.greetingId())
                                   .orElseThrow(() -> new HubUserException(
                                       HubUserErrors.GREETING_NOT_FOUND));

        Optional<GreetingMessage> optionalGreetingMessage = this.greetingRepository.findOne(
            GreetingRepository.Filters.findByGreetingTitle(input.greetingTitle()));

        if (optionalGreetingMessage.isPresent() &&
                !optionalGreetingMessage.get().getGreetingId().equals(greeting.getGreetingId())) {
            throw new HubUserException(HubUserErrors.ALREADY_GREETING);
        }

        greeting.greetingTitle(input.greetingTitle());
        greeting.greetingDetail(input.greetingDetail());


        this.greetingRepository.save(greeting);

        return new Output(greeting.getGreetingId());

    }


    }

