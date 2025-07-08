package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.CreateContactCommand;
import com.thitsaworks.operation_portal.core.participant.command.CreateLiquidityProfileCommand;
import com.thitsaworks.operation_portal.core.participant.command.ModifyContactCommand;
import com.thitsaworks.operation_portal.core.participant.command.ModifyLiquidityProfileCommand;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipantCommand;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantErrors;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantException;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModifyParticipantCommandHandler implements ModifyParticipantCommand {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantCommandHandler.class);

    private final ParticipantRepository participantRepository;

    private final ModifyContactCommand modifyContactCommand;

    private final CreateContactCommand createContactCommand;

    private final CreateLiquidityProfileCommand createLiquidityProfileCommand;

    private final ModifyLiquidityProfileCommand modifyLiquidityProfileCommand;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input)
            throws ParticipantException {

        Participant participant = this.participantRepository.findById(input.participantId())
                                                            .orElseThrow(() -> new ParticipantException(
                                                                    ParticipantErrors.PARTICIPANT_NOT_FOUND));

        this.participantRepository.save(participant
                        .name(input.companyName())
                        .address(input.address())
                        .mobile(input.mobile())
        );

        //For Contact Info
        if (input.contactInfoList() != null && !input.contactInfoList().isEmpty()) {

            for (var contact : input.contactInfoList()) {

                if (contact.contactId() != null) {

                    this.modifyContactCommand.execute(
                            new ModifyContactCommand.Input(participant.getParticipantId(),
                                                           contact.contactId(),
                                                           contact.name(),
                                                           contact.title(),
                                                           contact.email(),
                                                           contact.mobile(),
                                                           contact.contactType()));
                } else {

                    this.createContactCommand.execute(
                            new CreateContactCommand.Input(contact.name(),
                                                           contact.title(),
                                                           contact.email(),
                                                           contact.mobile(),
                                                           participant.getParticipantId(),
                                                           contact.contactType()));
                }
            }
        }

        //For Liquidity Profile
        if (!input.liquidityProfileInfoList().isEmpty()) {

            for (var liquidityProfile : input.liquidityProfileInfoList()) {

                if (liquidityProfile.liquidityProfileId() != null) {

                    this.modifyLiquidityProfileCommand.execute(new ModifyLiquidityProfileCommand.Input(participant.getParticipantId(),
                                                                                                       liquidityProfile.liquidityProfileId(),
                                                                                                       liquidityProfile.accountName(),
                                                                                                       liquidityProfile.accountNumber(),
                                                                                                       liquidityProfile.currency(),
                                                                                                       liquidityProfile.isActive()));
                } else {

                    this.createLiquidityProfileCommand.execute(new CreateLiquidityProfileCommand.Input(participant.getParticipantId(),
                                                                                                       liquidityProfile.accountName(),
                                                                                                       liquidityProfile.accountNumber(),
                                                                                                       liquidityProfile.currency(),
                                                                                                       liquidityProfile.isActive()));

                }
            }
        }

        return new ModifyParticipantCommand.Output(true, participant.getParticipantId());
    }

}
