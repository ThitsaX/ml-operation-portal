package com.thitsaworks.operation_portal.core.hub_services.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class HubParticipantDetailData implements Serializable {

    Integer participantId;

    String participantName;

    List<AccountData> accounts = new ArrayList<>();

    @Getter
    @Setter
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class AccountData implements Serializable {

        Integer participantCurrencyId;

        String currencyId;

        Integer ledgerAccountTypeId;

        String ledgerAccountTypeName;

    }

}
