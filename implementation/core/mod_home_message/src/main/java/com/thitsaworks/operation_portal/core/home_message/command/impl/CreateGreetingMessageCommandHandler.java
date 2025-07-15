package com.thitsaworks.operation_portal.core.home_message.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.home_message.command.CreateGreetingMessageCommand;
import com.thitsaworks.operation_portal.core.home_message.model.GreetingMessage;
import com.thitsaworks.operation_portal.core.home_message.model.repository.GreetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateGreetingMessageCommandHandler implements CreateGreetingMessageCommand {

    private final GreetingRepository greetingRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input){
        var greeting = new GreetingMessage(input.greetingTitle(),
                                           input.greetingTitle());

        this.greetingRepository.save(greeting);

        return  new Output(true);


    }

}
