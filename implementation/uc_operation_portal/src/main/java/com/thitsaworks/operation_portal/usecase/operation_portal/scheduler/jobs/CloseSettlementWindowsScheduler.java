package com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.jobs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.SettlementHubClient;
import com.thitsaworks.operation_portal.core.hub_services.api.GetSettlementWindows;
import com.thitsaworks.operation_portal.core.hub_services.api.PostCloseSettlementWindows;
import com.thitsaworks.operation_portal.core.hub_services.support.SettlementWindowState;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifyJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.core.settlement.data.SettlementModelData;
import com.thitsaworks.operation_portal.core.settlement.query.SettlementModelQuery;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.ScheduledJob;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component("CloseSettlementWindowsScheduler")
public class CloseSettlementWindowsScheduler
        extends ScheduledJob<SchedulerConfigData, List<PostCloseSettlementWindows.Response>> {

    private static final Logger LOG = LoggerFactory.getLogger(CloseSettlementWindowsScheduler.class);

    private final SettlementModelQuery settlementModelQuery;

    private final SettlementHubClient settlementHubClient;

    public CloseSettlementWindowsScheduler(CreateJobExecutionLogCommand createJobExecutionLogCommand,
                                           ModifyJobExecutionLogCommand modifyJobExecutionLogCommand,
                                           CreateInputAuditCommand createInputAuditCommand,
                                           CreateOutputAuditCommand createOutputAuditCommand,
                                           CreateExceptionAuditCommand createExceptionAuditCommand,
                                           ActionAuthorizationManager actionAuthorizationManager,
                                           ObjectMapper objectMapper,
                                           SettlementModelQuery settlementModelQuery,
                                           SettlementHubClient settlementHubClient) {

        super(createJobExecutionLogCommand, modifyJobExecutionLogCommand, createInputAuditCommand,
              createOutputAuditCommand, createExceptionAuditCommand, actionAuthorizationManager, objectMapper);

        this.settlementModelQuery = settlementModelQuery;
        this.settlementHubClient = settlementHubClient;
    }

    @Override
    protected List<PostCloseSettlementWindows.Response> onExecute(SchedulerConfigData schedulerConfigData)
        throws DomainException, InterruptedException {

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

        List<PostCloseSettlementWindows.Response> settlementWindowsList = new ArrayList<>();

        LocalDateTime runningTime = LocalDateTime.now(ZoneId.of(schedulerConfigData.zoneId())).withNano(0);

        String reason =
            String.format("Executed by Scheduler Config : [%s] of Settlement Model : [%s] at : [%s (%s)].",
                          schedulerConfigData.name(),
                          settlementModelData.name(),
                          runningTime,
                          schedulerConfigData.zoneId());

        for (var settlementWindow : settlementWindowList) {

            var output = this.settlementHubClient.closeSettlementWindows(settlementWindow.settlementWindowId(),
                                                            new PostCloseSettlementWindows.Request(
                                                                SettlementWindowState.CLOSED.toString(),
                                                                reason));

            output = new PostCloseSettlementWindows.Response(settlementWindow.settlementWindowId(),
                                                             SettlementWindowState.CLOSED.toString(),
                                                             output.reason(),
                                                             output.createdDate(),
                                                             output.closedDate(),
                                                             output.changedDate());

            settlementWindowsList.add(output);
        }

        return settlementWindowsList;

//        final int MAX_RETRIES = 6;
//        final int RETRY_DELAY_MS = 5000;
//
//        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
//
//            Thread.sleep(RETRY_DELAY_MS);
//
//            GetSettlementWindows.SettlementWindow latestWindowData =
//                settlementHubClient.getSettlementWindowById(settlementWindowIdList.getLast());
//
//            if (SettlementWindowState.CLOSED.name()
//                                            .equals(latestWindowData.state())) {
//
//                return settlementHubClient.createSettlement(new PostCreateSettlement.Request(settlementModelData.name(),
//                                                                                             reason,
//                                                                                             settlementWindowList.stream()
//                                                                                              .map(settlementWindow -> new SettlementWindowId(settlementWindow.settlementWindowId()))
//                                                                                              .toList()));
//            }
//
//        }
//
//        throw new HubServicesException(HubServicesErrors.SETTLEMENT_WINDOW_ERROR.description(
//            "Settlement window did not close properly within retry limit"));

    }

}
