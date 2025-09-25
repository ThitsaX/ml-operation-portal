package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.data.ContactData;
import com.thitsaworks.operation_portal.core.participant.query.ContactQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetContactList;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetContactListHandler extends OperationPortalUseCase<GetContactList.Input, GetContactList.Output>
    implements GetContactList {

    private static final Logger LOG = LoggerFactory.getLogger(GetContactListHandler.class);

    private final ContactQuery contactQuery;

    public GetContactListHandler(PrincipalCache principalCache,
                                 ActionAuthorizationManager actionAuthorizationManager,
                                 ContactQuery contactQuery) {

        super(principalCache,
              actionAuthorizationManager);

        this.contactQuery = contactQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        List<ContactData> contactDataList = this.contactQuery.getContacts(input.participantId());

        List<Output.ContactInfo> contactInfoList = new ArrayList<>();

        for (ContactData contactData : contactDataList) {

            contactInfoList.add(
                new Output.ContactInfo(contactData.contactId(),
                                       contactData.name(),
                                       contactData.position(),
                                       contactData.email(),
                                       contactData.mobile(),
                                       contactData.contactType()
                                                  .name()));
        }

        return new Output(contactInfoList);
    }

}
