package com.thitsaworks.operation_portal.core.home_message.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.home_message.command.UpdateGreetingCommand;
import com.thitsaworks.operation_portal.core.home_message.exception.GreetingErrors;
import com.thitsaworks.operation_portal.core.home_message.exception.GreetingException;
import com.thitsaworks.operation_portal.core.home_message.model.repository.GreetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateHomeMessageCommandHandler implements UpdateGreetingCommand {

    private final GreetingRepository greetingRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws GreetingException {

        var greeting = this.greetingRepository.findById(input.greetingId())
                                   .orElseThrow(() -> new GreetingException(
                                       GreetingErrors.GREETING_NOT_FOUND));

        greeting.greetingTitle(input.greetingTitle());
        greeting.greetingDetail(input.greetingDetail());


        this.greetingRepository.save(greeting);

        return new Output(greeting.getGreetingId());

    }


    }

