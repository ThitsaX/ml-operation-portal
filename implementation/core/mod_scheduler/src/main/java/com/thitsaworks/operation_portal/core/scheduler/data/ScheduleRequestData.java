package com.thitsaworks.operation_portal.core.scheduler.data;

import java.util.List;

public record ScheduleRequestData(String name, List<String> daysOfWeek, List<String> times, String description, boolean active) {}
