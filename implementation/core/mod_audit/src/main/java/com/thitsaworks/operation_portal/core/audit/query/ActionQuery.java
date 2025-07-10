package com.thitsaworks.operation_portal.core.audit.query;

import com.thitsaworks.operation_portal.core.audit.data.ActionData;

import java.util.List;
import java.util.Optional;

public interface ActionQuery {
    List<ActionData> getAction();

    Optional<ActionData> get(String Name) ;
}


