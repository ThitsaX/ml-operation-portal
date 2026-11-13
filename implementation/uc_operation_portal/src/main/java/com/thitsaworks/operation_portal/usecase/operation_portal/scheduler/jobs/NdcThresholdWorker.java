/*
 * Copyright (c) 2024-2026 ThitsaWorks Pte. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.jobs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.identifier.ThresholdConfigurationId;
import com.thitsaworks.operation_portal.component.common.type.JobStatus;
import com.thitsaworks.operation_portal.component.common.type.NdcThresholdStateType;
import com.thitsaworks.operation_portal.component.common.type.ThresholdScopeType;
import com.thitsaworks.operation_portal.component.misc.annotation.ActionMetadata;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.ActionCategory;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.hub_services.data.NdcUsedData;
import com.thitsaworks.operation_portal.core.hub_services.query.GetNdcUsedDataQuery;
import com.thitsaworks.operation_portal.core.notification.command.EvaluateNdcThresholdCommand;
import com.thitsaworks.operation_portal.core.notification.data.ThresholdConfigurationData;
import com.thitsaworks.operation_portal.core.notification.data.ThresholdGateDecision;
import com.thitsaworks.operation_portal.core.notification.model.ThresholdDetail;
import com.thitsaworks.operation_portal.core.notification.model.repository.ThresholdDetailRepository;
import com.thitsaworks.operation_portal.core.notification.query.ThresholdConfigurationQuery;
import com.thitsaworks.operation_portal.core.participant.data.ParticipantData;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantQuery;
import com.thitsaworks.operation_portal.core.scheduler.command.CreateJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.command.ModifyJobExecutionLogCommand;
import com.thitsaworks.operation_portal.core.scheduler.data.SchedulerConfigData;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.ScheduledJob;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component("NdcThresholdWorker")
@ActionMetadata(category = ActionCategory.SYSTEM_JOBS_AND_SCHEDULED_EXECUTORS)
public class NdcThresholdWorker
    extends ScheduledJob<SchedulerConfigData, List<NdcThresholdWorker.NdcEvaluation>> {

    private static final Logger LOG = LoggerFactory.getLogger(NdcThresholdWorker.class);

    @Override
    protected boolean shouldCreateAudit() {

        return false;
    }

    private final ThresholdConfigurationQuery thresholdConfigurationQuery;

    private final EvaluateNdcThresholdCommand evaluateNdcThresholdCommand;

    private final CreateJobExecutionLogCommand evaluationLogCreateCommand;

    private final ModifyJobExecutionLogCommand evaluationLogModifyCommand;

    private final GetNdcUsedDataQuery ndcUsedDataQuery;

    private final ThresholdDetailRepository thresholdDetailRepository;

    private final ParticipantQuery participantQuery;

    public NdcThresholdWorker(
        CreateJobExecutionLogCommand createJobExecutionLogCommand,
        ModifyJobExecutionLogCommand modifyJobExecutionLogCommand,
        CreateInputAuditCommand createInputAuditCommand,
        CreateOutputAuditCommand createOutputAuditCommand,
        CreateExceptionAuditCommand createExceptionAuditCommand,
        ActionAuthorizationManager actionAuthorizationManager,
        ObjectMapper objectMapper,
        ThresholdConfigurationQuery thresholdConfigurationQuery,
        EvaluateNdcThresholdCommand evaluateNdcThresholdCommand,
        GetNdcUsedDataQuery getNdcUsedDataQuery,
        ThresholdDetailRepository thresholdDetailRepository,
        ParticipantQuery participantQuery) {

        super(
            createJobExecutionLogCommand,
            modifyJobExecutionLogCommand,
            createInputAuditCommand,
            createOutputAuditCommand,
            createExceptionAuditCommand,
            actionAuthorizationManager,
            objectMapper
             );

        this.thresholdConfigurationQuery = thresholdConfigurationQuery;
        this.evaluateNdcThresholdCommand = evaluateNdcThresholdCommand;
        this.evaluationLogCreateCommand = createJobExecutionLogCommand;
        this.evaluationLogModifyCommand = modifyJobExecutionLogCommand;
        this.ndcUsedDataQuery = getNdcUsedDataQuery;
        this.thresholdDetailRepository = thresholdDetailRepository;
        this.participantQuery = participantQuery;
    }

    @Override
    protected List<NdcEvaluation> onExecute(SchedulerConfigData schedulerConfigData)
        throws DomainException {

        return this.evaluateThresholds(schedulerConfigData, schedulerConfigData.zoneId(), true, "schedule");
    }

    public List<NdcEvaluation> executeOnDemand() throws DomainException {

        return this.evaluateThresholds(null,
                                       ZoneId.systemDefault()
                                             .getId(),
                                       false,
                                       "balanceUpdate");
    }

    private List<NdcEvaluation> evaluateThresholds(SchedulerConfigData schedulerConfigData,
                                                   String zoneId,
                                                   boolean persistExecutionLog,
                                                   String actor)
        throws DomainException {

        var schemeConfiguration = thresholdConfigurationQuery.getSchemeConfiguration();

        if (schemeConfiguration.isEmpty() || !schemeConfiguration.get()
                                                                 .thresholdEnabled()) {
            LOG.info("Skipping NDC evaluation because the scheme gate is OFF or unavailable");
            return List.of();
        }

        LOG.info("Starting NDC threshold evaluation because the scheme gate is ON :");

        Map<ThresholdConfigurationId, ThresholdConfigurationData> dfspConfigurationsById =
            thresholdConfigurationQuery.getAll()
                                       .stream()
                                       .filter(configuration -> configuration.scopeType() == ThresholdScopeType.DFSP)
                                       .collect(Collectors.toMap(
                                           ThresholdConfigurationData::thresholdConfigurationId,
                                           Function.identity()
                                                                ));

        List<ThresholdDetail> enabledDetails = thresholdDetailRepository
                                                   .findAllByStatusTrueOrderByCurrencyAsc()
                                                   .stream()
                                                   .filter(detail -> isDetailGateAllowed(detail,
                                                                                         dfspConfigurationsById))
                                                   .toList();

        if (enabledDetails.isEmpty()) {
            LOG.info("Skipping NDC evaluation because no active DFSP/currency threshold details are enabled");
            return List.of();
        }

        Map<String, List<ThresholdDetail>> detailsByDfsp = enabledDetails.stream()
                                                                         .collect(Collectors.groupingBy(
                                                                             detail -> dfspConfigurationsById.get(detail.getThresholdConfigurationId())
                                                                                                             .dfspId(),
                                                                             LinkedHashMap::new,
                                                                             Collectors.toList()
                                                                                                       ));

        LOG.info("NDC threshold details resolved: dfspCount={}, currencyConfigCount={}",
                 detailsByDfsp.size(), enabledDetails.size());

        List<NdcEvaluation> evaluations = new ArrayList<>();
        int skippedEvaluations = 0;
        int ndcUsedRecords = 0;

        for (Map.Entry<String, List<ThresholdDetail>> entry : detailsByDfsp.entrySet()) {

            String dfspId = entry.getKey();
            GetNdcUsedDataQuery.Output output = ndcUsedDataQuery.execute(
                new GetNdcUsedDataQuery.Input(dfspId));
            Log.info("Output result :" + output.getNdcUsedData()
                                               .toString());

            if (output == null || output.getNdcUsedData() == null) {
                LOG.warn("Skipping NDC evaluation because no NDC used data exists for DFSP [{}]", dfspId);
                skippedEvaluations +=
                    entry.getValue()
                         .size();
                continue;
            }

            Map<String, NdcUsedData> ndcUsedByCurrency = output.getNdcUsedData()
                                                               .stream()
                                                               .filter(NdcUsedData::isActive)
                                                               .collect(Collectors.toMap(
                                                                   NdcUsedData::currency,
                                                                   Function.identity(),
                                                                   (first, ignored) -> first,
                                                                   LinkedHashMap::new
                                                                                        ));

            ndcUsedRecords += ndcUsedByCurrency.size();

            String dfspDisplayName = participantQuery.get(dfspId)
                .map(ParticipantData::description)
                .map(String::trim)
                .filter(name -> !name.isBlank())
                .orElse(dfspId);

            for (ThresholdDetail detail : entry.getValue()) {

                NdcUsedData ndcUsedData = ndcUsedByCurrency.get(detail.getCurrency());
                if (ndcUsedData == null || ndcUsedData.ndcUsed() == null) {
                    LOG.warn("Skipping NDC evaluation because NDC used percent is missing for [{} / {}]",
                             dfspId, detail.getCurrency());
                    skippedEvaluations++;
                    continue;
                }

                Instant
                    evaluatedAt =
                    Instant.now()
                           .truncatedTo(ChronoUnit.SECONDS);
                LocalDateTime evaluationLogTime = LocalDateTime.now(ZoneId.of(zoneId))
                                                               .withNano(0);
                LOG.info("NDC evaluated at: {}", evaluatedAt);

                BigDecimal ndcUsedPercent = ndcUsedData.ndcUsed();
                String comparison = ndcUsedPercent.compareTo(detail.getNdcConfig()) >= 0
                                        ? "AT_OR_ABOVE_THRESHOLD"
                                        : "BELOW_THRESHOLD";

                LOG.info("NDC calculation result: participant={}, currency={}, currentPosition={}, "
                             + "ndcLimit={}, ndcUsedPercent={}, thresholdPercent={}, comparison={}, "
                             + "calculationSource=GetNdcUsedDataQuery",
                         dfspId, detail.getCurrency(), ndcUsedData.currentPosition(), ndcUsedData.ndc(), ndcUsedPercent,
                         detail.getNdcConfig(), comparison);

                EvaluateNdcThresholdCommand.Output thresholdOutput = evaluateNdcThresholdCommand.execute(
                    new EvaluateNdcThresholdCommand.Input(
                        dfspId,
                        dfspDisplayName,
                        detail.getCurrency(),
                        ndcUsedData.currentPosition(),
                        ndcUsedData.ndc(),
                        ndcUsedPercent,
                        detail.getNdcConfig(),
                        evaluatedAt,
                        actor
                    )
                                                                                                        );

                NdcEvaluation evaluation = new NdcEvaluation(
                    dfspId,
                    detail.getCurrency(),
                    ndcUsedPercent,
                    detail.getNdcConfig(),
                    thresholdOutput.previousState(),
                    thresholdOutput.currentState(),
                    thresholdOutput.breachCycleNo(),
                    thresholdOutput.alertCreated(),
                    thresholdOutput.recovered(),
                    thresholdOutput.alertEventId()
                );

                String decision;
                if (thresholdOutput.alertCreated()) {
                    decision = "CREATE_ALERT";
                } else if (thresholdOutput.recovered()) {
                    decision = "RECOVERED";
                } else if (thresholdOutput.currentState() == NdcThresholdStateType.BREACHED) {
                    decision = "SUPPRESS_DUPLICATE";
                } else {
                    decision = "NO_ALERT";
                }

                LOG.info("NDC evaluation decision: participant={}, currency={}, previousState={}, "
                             + "currentState={}, breachCycle={}, decision={}, alertEventId={}",
                         evaluation.participantName(), evaluation.currency(), evaluation.previousState(),
                         evaluation.currentState(), evaluation.breachCycleNo(), decision,
                         evaluation.alertEventId());

                if (persistExecutionLog) {
                    persistEvaluationLog(
                        schedulerConfigData,
                        evaluation,
                        evaluationLogTime);
                }
                evaluations.add(evaluation);
            }
        }

        long
            alertsCreated =
            evaluations.stream()
                       .filter(NdcEvaluation::alertCreated)
                       .count();
        long
            recoveries =
            evaluations.stream()
                       .filter(NdcEvaluation::recovered)
                       .count();

        LOG.info("NDC worker completed: ndcUsedRecords={}, evaluated={}, skipped={}, "
                     + "alertsCreated={}, recovered={}",
                 ndcUsedRecords, evaluations.size(), skippedEvaluations,
                 alertsCreated, recoveries);

        return evaluations;
    }

    private boolean isDetailGateAllowed(
        ThresholdDetail detail,
        Map<ThresholdConfigurationId, ThresholdConfigurationData> dfspConfigurationsById) {

        ThresholdConfigurationData configuration =
            dfspConfigurationsById.get(detail.getThresholdConfigurationId());

        if (configuration == null) {
            LOG.warn("Skipping threshold detail [{}]: linked DFSP configuration [{}] is missing",
                     detail.getThresholdDetailId(), detail.getThresholdConfigurationId());
            return false;
        }

        ThresholdGateDecision gate = thresholdConfigurationQuery.checkGate(configuration.dfspId());
        if (!gate.allowed()) {
            LOG.info("Skipping threshold detail [{} / {}]: {}",
                     configuration.dfspId(), detail.getCurrency(), gate.reason());
        }

        return gate.allowed();
    }

    private void persistEvaluationLog(SchedulerConfigData schedulerConfigData,
                                      NdcEvaluation evaluation,
                                      LocalDateTime evaluatedAt) throws DomainException {

        var started = evaluationLogCreateCommand.execute(
            new CreateJobExecutionLogCommand.Input(
                schedulerConfigData.name(),
                JobStatus.STARTED,
                evaluatedAt,
                evaluation.participantName(),
                evaluation.currency(),
                evaluation.ndcUsedPercent(),
                evaluation.thresholdPercent()
            )
                                                        );

        String message = String.format(
            "NDC evaluated: used=%s%%, threshold=%s%%, state=%s -> %s, alertCreated=%s, recovered=%s",
            evaluation.ndcUsedPercent(),
            evaluation.thresholdPercent(),
            evaluation.previousState(),
            evaluation.currentState(),
            evaluation.alertCreated(),
            evaluation.recovered()
                                      );

        evaluationLogModifyCommand.execute(
            new ModifyJobExecutionLogCommand.Input(
                started.jobExecutionLogId(),
                JobStatus.COMPLETED,
                message,
                evaluatedAt,
                evaluation.participantName(),
                evaluation.currency(),
                evaluation.ndcUsedPercent(),
                evaluation.thresholdPercent()
            )
                                          );
    }

    public record NdcEvaluation(
        String participantName,
        String currency,
        BigDecimal ndcUsedPercent,
        BigDecimal thresholdPercent,
        NdcThresholdStateType previousState,
        NdcThresholdStateType currentState,
        long breachCycleNo,
        boolean alertCreated,
        boolean recovered,
        com.thitsaworks.operation_portal.component.common.identifier.NdcAlertEventId alertEventId
    ) {
    }

}
