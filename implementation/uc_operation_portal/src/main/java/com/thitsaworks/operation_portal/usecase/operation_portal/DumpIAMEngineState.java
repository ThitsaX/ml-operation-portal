package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;
import com.thitsaworks.operation_portal.core.iam.data.EngineData;

public interface DumpIAMEngineState extends UseCase<DumpIAMEngineState.Input, DumpIAMEngineState.Output> {

    record Input() { }

    record Output(EngineData engineState) { }

}
