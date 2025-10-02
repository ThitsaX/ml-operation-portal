package com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.jobs;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.SettlementHubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.GetSettlementWindowsList;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCloseSettlementWindows;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCreateSettlement;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindowId;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindowState;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifyJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.ScheduledJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("CloseSettlementWindowsScheduler")
public class CloseSettlementWindowsScheduler extends ScheduledJob {

    private static final Logger LOG = LoggerFactory.getLogger(CloseSettlementWindowsScheduler.class);

    private final SettlementHubClient settlementHubClient;

    public CloseSettlementWindowsScheduler(CreateJobExecutionLogCommand createJobExecutionLogCommand,
                                           ModifyJobExecutionLogCommand modifyJobExecutionLogCommand,
                                           SettlementHubClient settlementHubClient) {

        super(createJobExecutionLogCommand, modifyJobExecutionLogCommand);

        this.settlementHubClient = settlementHubClient;
    }

    @Override
    protected void onExecute(SchedulerConfigData schedulerConfigData) throws DomainException, InterruptedException {

        //TODO: call actual dynamic getSettlementModels when settlementModels are implemented
        String settlementModel = "DEFERREDNET";

        List<GetSettlementWindowsList.SettlementWindow> settlementWindowList =
                this.settlementHubClient.getSettlementWindowsList(
                        null,
                        null,
                        null,
                        SettlementWindowState.OPEN.toString(),
                        null,
                        new GetSettlementWindowsList.Request());

        LOG.info("Settlement Window List: {}", settlementWindowList);

        List<SettlementWindowId> settlementWindowIdList = new ArrayList<>();

        //Close each settlement window
        for (var settlementWindow : settlementWindowList) {

            this.settlementHubClient.closeSettlementWindows(settlementWindow.settlementWindowId(),
                                                            new PostCloseSettlementWindows.Request(
                                                                    "CLOSED", ""));

            settlementWindowIdList.add(new SettlementWindowId(settlementWindow.settlementWindowId()));
        }

        //Create a new settlement with closed windows
        this.settlementHubClient.createSettlement(new PostCreateSettlement.Request(settlementModel,
                                                                                   "",
                                                                                   settlementWindowIdList));

    }

}
