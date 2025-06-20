package com.thitsaworks.operation_portal.core.participant.data;

import com.thitsaworks.operation_portal.component.common.identifier.ContactId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.common.type.DfspCode;
import com.thitsaworks.operation_portal.component.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantUser;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

public record ParticipantData(ParticipantId participantId,
                              String name,
                              DfspCode dfspCode,
                              String dfspName,
                              String address,
                              Mobile mobile,
                              ContactId businessContactId,
                              ContactId technicalContactId,
                              Long createdDate,
                              Set<ParticipantUserId> participantUserIds) implements Serializable {

    public ParticipantData(Participant participant) {

        this(participant.getParticipantId(),

             participant.getName(),

             participant.getDfspCode(),

             participant.getDfspName(),

             participant.getAddress(),

             participant.getMobile(),

             participant.getBusinessContact() != null ? participant.getBusinessContact().getContactId() : null,

             participant.getTechnicalContact() != null ? participant.getTechnicalContact().getContactId() : null,

             participant.getCreatedAt().getEpochSecond(),

             participant.getParticipantUsers().stream()
                        .map((ParticipantUser participantUser) -> new ParticipantUserId(
                                participantUser.getParticipantUserId().getId())).collect(Collectors.toSet()));

    }

}
