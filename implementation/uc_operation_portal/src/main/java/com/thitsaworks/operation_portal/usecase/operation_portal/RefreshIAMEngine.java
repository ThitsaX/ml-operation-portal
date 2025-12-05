package com.thitsaworks.operation_portal.usecase.operation_portal;

import com.thitsaworks.operation_portal.component.misc.usecase.UseCase;

public interface RefreshIAMEngine extends UseCase<RefreshIAMEngine.Input, RefreshIAMEngine.Output> {

    record Input() { }

    record Output(boolean refreshed) { }

}
