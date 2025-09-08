package com.thitsaworks.operation_portal.core.participant.data;

import com.thitsaworks.operation_portal.component.common.identifier.ParticipantId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.Mobile;
import com.thitsaworks.operation_portal.component.common.type.ParticipantName;
import com.thitsaworks.operation_portal.core.participant.model.Participant;
import com.thitsaworks.operation_portal.core.participant.model.User;

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
                              Set<UserId> userIds) implements Serializable {

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
             participant.getUsers()
                        .stream()
                        .map((User user) -> new UserId(
                                user.getUserId()
                                    .getId()))
                        .collect(Collectors.toSet()));

    }

}
