package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.CreateContact;
import com.thitsaworks.operation_portal.core.participant.command.CreateLiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.command.ModifyContact;
import com.thitsaworks.operation_portal.core.participant.command.ModifyLiquidityProfile;
import com.thitsaworks.operation_portal.core.participant.command.ModifyParticipant;
import com.thitsaworks.operation_portal.core.participant.exception.ContactNotFoundException;
import com.thitsaworks.operation_portal.core.participant.exception.LiquidityProfileNotFoundException;
import com.thitsaworks.operation_portal.core.participant.exception.ParticipantNotFoundException;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ModifyParticipantHandler implements ModifyParticipant {

    private static final Logger LOG = LoggerFactory.getLogger(ModifyParticipantHandler.class);

    private final ParticipantRepository participantRepository;

    private final ModifyContact modifyContact;

    private final CreateContact createContact;

    private final CreateLiquidityProfile createLiquidityProfile;

    private final ModifyLiquidityProfile modifyLiquidityProfile;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input)
            throws ParticipantNotFoundException, ContactNotFoundException, LiquidityProfileNotFoundException {

        Optional<Participant> optionalParticipant = this.participantRepository.findById(input.participantId());

        if (optionalParticipant.isEmpty()) {

            throw new ParticipantNotFoundException(input.participantId().getId().toString());

        }

        Participant participant = optionalParticipant.get();

        this.participantRepository.save(participant
                        .name(input.companyName())
                        .address(input.address())
                        .mobile(input.mobile())
        );

        //For Contact Info
        if (input.contactInfoList() != null && !input.contactInfoList().isEmpty()) {

            for (var contact : input.contactInfoList()) {

                if (contact.contactId() != null) {

                    this.modifyContact.execute(
                            new ModifyContact.Input(participant.getParticipantId(),
                                                    contact.contactId(),
                                                    contact.name(),
                                                    contact.title(),
                                                    contact.email(),
                                                    contact.mobile(),
                                                    contact.contactType()));
                } else {

                    this.createContact.execute(
                            new CreateContact.Input(contact.name(),
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

                    this.modifyLiquidityProfile.execute(new ModifyLiquidityProfile.Input(participant.getParticipantId(),
                                                                                         liquidityProfile.liquidityProfileId(),
                                                                                         liquidityProfile.accountName(),
                                                                                         liquidityProfile.accountNumber(),
                                                                                         liquidityProfile.currency(),
                                                                                         liquidityProfile.isActive()));
                } else {

                    this.createLiquidityProfile.execute(new CreateLiquidityProfile.Input(participant.getParticipantId(),
                                                                                         liquidityProfile.accountName(),
                                                                                         liquidityProfile.accountNumber(),
                                                                                         liquidityProfile.currency(),
                                                                                         liquidityProfile.isActive()));

                }
            }
        }

        return new ModifyParticipant.Output(true, participant.getParticipantId());
    }

}
