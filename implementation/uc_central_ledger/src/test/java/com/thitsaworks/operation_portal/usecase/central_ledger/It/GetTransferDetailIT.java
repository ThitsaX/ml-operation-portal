package com.thitsaworks.operation_portal.usecase.central_ledger.It;

import com.thitsaworks.operation_portal.usecase.CentralLedgerUseCaseConfiguration;
import com.thitsaworks.operation_portal.usecase.central_ledger.TestSettings;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
                CentralLedgerUseCaseConfiguration.class, TestSettings.class})
public class GetTransferDetailIT {
}
