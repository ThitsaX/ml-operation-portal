package com.thitsaworks.dfsp_portal.usecase.hub_operator;

import com.thitsaworks.dfsp_portal.component.usecase.AbstractAuditableUseCase;
import com.thitsaworks.dfsp_portal.hubuser.identity.HubUserId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public abstract class ModifyExistingHubUser
        extends AbstractAuditableUseCase<ModifyExistingHubUser.Input, ModifyExistingHubUser.Output> {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Input {

        private HubUserId hubUserId;

        private String name;

        private String firstName;

        private String lastName;

        private String jobTitle;

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Output {

        private boolean modified;

        private HubUserId hubUserId;

    }
}
