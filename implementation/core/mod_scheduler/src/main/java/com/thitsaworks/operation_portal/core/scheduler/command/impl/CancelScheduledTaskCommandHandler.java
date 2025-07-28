package com.thitsaworks.operation_portal.core.scheduler.command.impl;

import com.thitsaworks.operation_portal.core.scheduler.command.CancelScheduledTaskCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Service
@RequiredArgsConstructor
public class CancelScheduledTaskCommandHandler implements CancelScheduledTaskCommand {

    private final Map<String, ScheduledFuture<?>> scheduledTasks;

    @Override
    public Output execute(String taskName) {
        ScheduledFuture<?> task = scheduledTasks.remove(taskName);
        if (task != null) {
            task.cancel(false);
            return new Output(true);
        }
        return new Output(false);
    }
}
