package com.thitsaworks.operation_portal.core.reporting.download.generator;

public interface ReportGenerator {

    boolean generateNextPending();

    long countRunning();

    record Settings(int reportPageSize){}
}
