package com.thitsaworks.operation_portal.core.hub_services.data;

import java.io.Serializable;

public record HubParticipantAccountData(Integer participantId,
                                        String participantName,
                                        Integer participantCurrencyId,
                                        String currencyId,
                                        Integer ledgerAccountTypeId,
                                        String ledgerAccountTypeName) implements Serializable {}
