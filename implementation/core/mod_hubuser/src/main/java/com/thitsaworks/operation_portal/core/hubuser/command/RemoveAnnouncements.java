package com.thitsaworks.operation_portal.core.hubuser.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public interface RemoveAnnouncements {

    @Getter
    @NoArgsConstructor
    class Input {

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    class Output {

        private boolean removed;

    }

    RemoveAnnouncements.Output execute(RemoveAnnouncements.Input input);

}
