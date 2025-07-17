package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreateParticipantCommandHandler implements CreateParticipantCommand {

    private static final Logger LOG = LoggerFactory.getLogger(CreateParticipantCommandHandler.class);

    private final ParticipantRepository participantRepository;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) throws ParticipantException {

        Optional<Participant> participantByDfspCode = this.participantRepository.findByDfspCode(input.dfspCode());

        if (participantByDfspCode.isEmpty()) {

            Participant participant = new Participant(input.dfspCode(),
                                                      input.name(),
                                                      input.dfspName(),
                                                      input.address(),
                                                      input.mobile(),
                                                      input.logoType(),
                                                      input.logo());

            //For ContactInfo
            if (input.contactInfoList() != null && !input.contactInfoList()
                                                         .isEmpty()) {

                for (var contact : input.contactInfoList()) {

                    participant.addContact(contact.name(),
                                           contact.title(),
                                           contact.email(),
                                           contact.mobile(),
                                           contact.contactType());
                }
            }

            //For Liquidity Profile
            if (input.liquidityProfileInfoList() != null && !input.liquidityProfileInfoList()
                                                                  .isEmpty()) {

                for (var liquidityProfile : input.liquidityProfileInfoList()) {

                    participant.addLiquidityProfile(liquidityProfile.bankName(),
                                                    liquidityProfile.accountName(),
                                                    liquidityProfile.accountNumber(),
                                                    liquidityProfile.currency(),
                                                    liquidityProfile.isActive());
                }
            }

            this.participantRepository.save(participant);

            return new CreateParticipantCommand.Output(true, participant.getParticipantId());

        } else {

            throw new ParticipantException(ParticipantErrors.PARTICIPANT_ALREADY_REGISTER);
        }
    }

}
