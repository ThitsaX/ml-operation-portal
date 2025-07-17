package com.thitsaworks.operation_portal.core.hubuser.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.hubuser.command.CreateGreetingMessageCommand;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserErrors;
import com.thitsaworks.operation_portal.core.hubuser.exception.HubUserException;
import com.thitsaworks.operation_portal.core.hubuser.model.GreetingMessage;
import com.thitsaworks.operation_portal.core.hubuser.model.repository.GreetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateGreetingMessageCommandHandler implements CreateGreetingMessageCommand {

    private final GreetingRepository greetingRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws HubUserException {
        Optional<GreetingMessage> optionalGreetingMessage = this.greetingRepository.findOne(
            GreetingRepository.Filters.findByGreetingTitle(input.greetingTitle()));

        if (optionalGreetingMessage.isPresent()) {

            throw new HubUserException(HubUserErrors.ALREADY_GREETING);
        }

        var greeting = new GreetingMessage(input.greetingTitle(),
                                           input.greetingDetail(),
                                           input.greetingDate());

        this.greetingRepository.save(greeting);

        return  new Output(true);


    }

}
