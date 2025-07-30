package com.thitsaworks.operation_portal.core.test_iam.cache.proxy;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.type.iamtesttype.ActionCode;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.test_iam.cache.IAMActionCache;
import com.thitsaworks.operation_portal.core.test_iam.data.ActionData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.IAMAction;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.IAMActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Primary
@Component
@Qualifier(CacheQualifiers.PROXY)
public class ProxyIAMActionCache implements IAMActionCache {
    @Autowired
    private IAMActionRepository iamActionRepository;

    @Autowired
    @Qualifier(CacheQualifiers.REDIS)
    private IAMActionCache iamActionCache;

    @Override
    public void save(ActionData actionData) {
        this.iamActionCache.save(actionData);

    }

    @Override
    public Optional<ActionData> find(ActionId actionId) throws IAMException {

        Optional<ActionData> actionData = this.iamActionCache.find(actionId);

        if (actionData.isEmpty()) {
            var iamAction = this.iamActionRepository.findById(actionId)
                                                    .orElseThrow(() -> new IAMException(IAMErrors.ACTION_NOT_FOUND));

            actionData = Optional.of(new ActionData(iamAction.getActionId(),
                                                    iamAction.getActionCode(),
                                                    iamAction.getScope(),
                                                    iamAction.getDescription()));
            this.iamActionCache.save(actionData.get());
        }

        return actionData;
    }

    @Override
    public ActionData get(ActionCode actionCode) throws IAMException {

        ActionData actionData = this.iamActionCache.get(actionCode);

        if (actionData == null) {
            var iamAction = this.iamActionRepository.findOne(IAMActionRepository.Filters.withActionCode(actionCode))
                                                    .orElseThrow(() -> new IAMException(IAMErrors.ACTION_NOT_FOUND));

            actionData = new ActionData(iamAction.getActionId(),
                                        iamAction.getActionCode(),
                                        iamAction.getScope(),
                                        iamAction.getDescription());
            this.iamActionCache.save(actionData);
        }

        return actionData;
    }

    @Override
    public List<ActionData> getAll() {

        List<ActionData> iamActions = this.iamActionCache.getAll();

        if (iamActions.isEmpty()) {
            List<ActionData> actions = new ArrayList<>();
            for (IAMAction iamAction : this.iamActionRepository.findAll()) {
                ActionData actionData = new ActionData(iamAction.getActionId(),
                                                       iamAction.getActionCode(),
                                                       iamAction.getScope(),
                                                       iamAction.getDescription());
                actions.add(actionData);
            }
            iamActions=actions;
        }
        return iamActions;
    }
    @Override
    public void delete(ActionId actionId) {
        this.iamActionCache.delete(actionId);

    }

}
