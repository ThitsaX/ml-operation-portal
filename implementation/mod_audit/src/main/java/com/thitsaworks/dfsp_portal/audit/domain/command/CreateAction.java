package com.thitsaworks.dfsp_portal.audit.domain.command;

import com.thitsaworks.dfsp_portal.audit.identity.ActionId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface CreateAction {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    class Input {

        private String name;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Output {

        private boolean created;

        private ActionId actionId;

    }

    Output execute(Input input);

}
