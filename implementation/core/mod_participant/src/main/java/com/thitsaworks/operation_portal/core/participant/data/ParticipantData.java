package com.thitsaworks.operation_portal.core.participant.data;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.ParticipantUserId;
import com.thitsaworks.operation_portal.component.common.type.ParticipantName;
import com.thitsaworks.operation_portal.component.common.type.Mobile;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantUser;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

public record ParticipantData(ParticipantId participantId,
                              String description,
                              ParticipantName participantName,
                              String address,
                              Mobile mobile,
                              String logoDataType,
                              byte[] logo,
                              Long createdDate,
                              Set<ParticipantUserId> participantUserIds) implements Serializable {

    public ParticipantData(Participant participant) {

        this(participant.getParticipantId(),
             participant.getDescription(),
             participant.getParticipantName(),
             participant.getAddress(),
             participant.getMobile(),
             participant.getLogoDatatype(),
             participant.getLogoBase64(),
             participant.getCreatedAt()
                        .getEpochSecond(),
             participant.getParticipantUsers()
                        .stream()
                        .map((ParticipantUser participantUser) -> new ParticipantUserId(
                            participantUser.getParticipantUserId()
                                           .getId()))
                        .collect(Collectors.toSet()));

    }

}
