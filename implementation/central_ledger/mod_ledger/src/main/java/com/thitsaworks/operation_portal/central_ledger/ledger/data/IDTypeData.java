package com.thitsaworks.operation_portal.central_ledger.ledger.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class IDTypeData implements Serializable {

    private String partyIdentifierTypeId;

    private String name;

}
