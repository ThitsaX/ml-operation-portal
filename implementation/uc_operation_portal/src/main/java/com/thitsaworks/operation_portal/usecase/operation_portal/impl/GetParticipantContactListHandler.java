package com.thitsaworks.operation_portal.usecase.operation_portal.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ContactData;
import com.thitsaworks.operation_portal.core.participant.query.ContactQuery;

import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.core_services.GetParticipantContactList;
import org.springframework.stereotype.Service;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GetParticipantContactListHandler extends OperationPortalAuditableUseCase<GetParticipantContactList.Input,GetParticipantContactList.Output>
    implements GetParticipantContactList {

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final ContactQuery contactQuery;

    public GetParticipantContactListHandler(CreateInputAuditCommand createInputAuditCommand,
                                            CreateOutputAuditCommand createOutputAuditCommand,
                                            CreateExceptionAuditCommand createExceptionAuditCommand,
                                            ObjectMapper objectMapper,
                                            PrincipalCache principalCache, ContactQuery contactQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);
        this.contactQuery = contactQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException, ConnectException {


        List<ContactData> contactDataList =this.contactQuery.getContacts(input.participantId());

        List<Output.ContactInfo> contactInfoList = new ArrayList<>();
        for (ContactData contactData : contactDataList) {
            GetParticipantContactList.Output.ContactInfo contactInfo = new GetParticipantContactList.Output.ContactInfo(
                contactData.contactId(),
                contactData.name(),
                contactData.title(),
                contactData.email(),
                contactData.mobile(),
                contactData.contactType()
            );
            contactInfoList.add(contactInfo);
        }

        return new GetParticipantContactList.Output(contactInfoList);

    }

}
