package com.thitsaworks.operation_portal.core.hub_services.data;

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
