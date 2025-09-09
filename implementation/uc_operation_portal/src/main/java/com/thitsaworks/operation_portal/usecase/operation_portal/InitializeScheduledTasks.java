package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

import java.util.List;

public interface InitializeScheduledTasks
    extends UseCase<InitializeScheduledTasks.Input, InitializeScheduledTasks.Output> {

    record Input(List<ConfigChange> changes) { 
        public record ConfigChange(
            String taskName,
            String cronExpression,
            ChangeType changeType
        ) {}
        
        public enum ChangeType {
            NEW, UPDATED, REMOVED
        }
    }

    record Output(boolean success, String message) {}
}
