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
package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.thitsaworks.operation_portal.component.common.type.ThresholdScopeType;
import com.thitsaworks.operation_portal.component.misc.annotation.ActionMetadata;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.component.misc.util.ActionCategory;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.notification.data.ThresholdConfigurationData;
import com.thitsaworks.operation_portal.core.notification.query.ThresholdConfigurationQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.GetDfspThresholdConfiguration;
import com.thitsaworks.operation_portal.usecase.util.action.ActionAuthorizationManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@ActionMetadata(category = ActionCategory.PARTICIPANT_PROFILE_AND_FINANCIAL_CONFIGURATION)
public class GetDfspThresholdConfigurationHandler
    extends OperationPortalUseCase<GetDfspThresholdConfiguration.Input,
                                    GetDfspThresholdConfiguration.Output>
    implements GetDfspThresholdConfiguration {

    private final ThresholdConfigurationQuery thresholdConfigurationQuery;

    public GetDfspThresholdConfigurationHandler(PrincipalCache principalCache,
                                                ActionAuthorizationManager actionAuthorizationManager,
                                                ThresholdConfigurationQuery thresholdConfigurationQuery) {
        super(principalCache, actionAuthorizationManager);
        this.thresholdConfigurationQuery = thresholdConfigurationQuery;
    }


    @Override
    protected Output onExecute(Input input) throws DomainException {

        Optional<ThresholdConfigurationData> thresholdConfiguration = this.thresholdConfigurationQuery.getDfspConfiguration(input.dfspId());
        Optional<ThresholdConfigurationData> schemeConfiguration = this.thresholdConfigurationQuery.getSchemeConfiguration();

        boolean schemeEnabled = schemeConfiguration.map(ThresholdConfigurationData::thresholdEnabled).orElse(false);
        if (thresholdConfiguration.isEmpty()) {
            return new Output(
                null,
                ThresholdScopeType.DFSP,
                input.dfspId(),
                false,
                schemeEnabled,
                null,
                null,
                null,
                null,
                null
            );
        }
        return new Output(
                thresholdConfiguration.get().thresholdConfigurationId(),
                thresholdConfiguration.get().scopeType(),
                thresholdConfiguration.get().dfspId(),
                thresholdConfiguration.get().thresholdEnabled(),
                schemeEnabled,
                thresholdConfiguration.get().status(),
                thresholdConfiguration.get().createdAt(),
                thresholdConfiguration.get().createdBy(),
                thresholdConfiguration.get().updatedAt(),
                thresholdConfiguration.get().updatedBy()
        );
    }

}
