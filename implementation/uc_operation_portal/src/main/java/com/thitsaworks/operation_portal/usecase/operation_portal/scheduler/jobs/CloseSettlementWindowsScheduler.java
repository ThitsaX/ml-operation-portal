package com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.jobs;

import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.hub_services.SettlementHubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.GetSettlementWindows;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCloseSettlementWindows;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCreateSettlement;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindowId;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindowState;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifyJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.settlement.data.SettlementModelData;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementModelQuery;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.ScheduledJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component("CloseSettlementWindowsScheduler")
public class CloseSettlementWindowsScheduler extends ScheduledJob {

    private static final Logger LOG = LoggerFactory.getLogger(CloseSettlementWindowsScheduler.class);

    private final SettlementModelQuery settlementModelQuery;

    private final SettlementHubClient settlementHubClient;

    public CloseSettlementWindowsScheduler(CreateJobExecutionLogCommand createJobExecutionLogCommand,
                                           ModifyJobExecutionLogCommand modifyJobExecutionLogCommand,
                                           SettlementModelQuery settlementModelQuery,
                                           SettlementHubClient settlementHubClient) {

        super(createJobExecutionLogCommand, modifyJobExecutionLogCommand);

        this.settlementModelQuery = settlementModelQuery;
        this.settlementHubClient = settlementHubClient;
    }

    @Override
    protected void onExecute(SchedulerConfigData schedulerConfigData) throws DomainException, InterruptedException {

        SettlementModelData settlementModelData =
                this.settlementModelQuery.get(schedulerConfigData.schedulerConfigId());

        List<GetSettlementWindows.SettlementWindow> settlementWindowList =
                this.settlementHubClient.getSettlementWindowsList(
                        null,
                        null,
                        settlementModelData.currencyId(),
                        SettlementWindowState.OPEN.toString(),
                        null,
                        new GetSettlementWindows.Request());

        LOG.info("Settlement Window List: {}", settlementWindowList);

        List<SettlementWindowId> settlementWindowIdList = new ArrayList<>();

        LocalDateTime runningTime = LocalDateTime.now(ZoneId.of(schedulerConfigData.zoneId())).withNano(0);

        String reason =
                String.format("Executed by Scheduler Config : [%s] of Settlement Model : [%s] at : [%s (%s)].",
                              schedulerConfigData.name(),
                              settlementModelData.name(),
                              runningTime,
                              schedulerConfigData.zoneId());

        //Close each settlement window
        for (var settlementWindow : settlementWindowList) {


            this.settlementHubClient.closeSettlementWindows(settlementWindow.settlementWindowId(),
                                                            new PostCloseSettlementWindows.Request(
                                                                    SettlementWindowState.CLOSED.toString(),
                                                                    reason));

            settlementWindowIdList.add(new SettlementWindowId(settlementWindow.settlementWindowId()));
        }

        //Create a new settlement with closed windows
        this.settlementHubClient.createSettlement(new PostCreateSettlement.Request(settlementModelData.name(),
                                                                                   reason,
                                                                                   settlementWindowIdList));

    }

}
