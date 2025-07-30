package com.thitsaworks.operation_portal.core.test_iam.cache.proxy;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.BlockedActionId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.test_iam.cache.BlockedActionCache;
import com.thitsaworks.operation_portal.core.test_iam.data.BlockedActionData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.BlockedAction;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.BlockedActionRepository;
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
public class ProxyBlockedActionCache implements BlockedActionCache {

    @Autowired
    private BlockedActionRepository blockedActionRepository;

    @Autowired
    @Qualifier(CacheQualifiers.REDIS)
    private BlockedActionCache blockedActionCache;

    @Override
    public void save(BlockedActionData blockedActionData) {

        this.blockedActionCache.save(blockedActionData);
    }

    @Override
    public void delete(BlockedActionId blockedActionId) {

        this.blockedActionCache.delete(blockedActionId);
    }

    @Override
    public Optional<BlockedActionData> find(BlockedActionId blockedActionId) throws IAMException {

        BlockedActionData blockedActionData = this.blockedActionCache.get(blockedActionId);

        if (blockedActionData == null) {
            Optional<BlockedAction> optionalBlockedAction =
                this.blockedActionRepository.findById(blockedActionId);

            if (optionalBlockedAction.isEmpty()) {
                return Optional.empty();
            }

            blockedActionData = new BlockedActionData(optionalBlockedAction.get());

            this.blockedActionCache.save(blockedActionData);
        }
        return Optional.of(blockedActionData);
    }

    @Override
    public BlockedActionData get(BlockedActionId blockedActionId) throws IAMException {

        BlockedActionData blockedActionData = this.blockedActionCache.get(blockedActionId);

        if (blockedActionData == null) {
            Optional<BlockedAction> optionalBlockedAction =
                this.blockedActionRepository.findById(blockedActionId);

            if (optionalBlockedAction.isEmpty()) {
                throw new IAMException(IAMErrors.BLOCKED_ACTION_NOT_FOUND);
            }

            blockedActionData = new BlockedActionData(optionalBlockedAction.get());
            this.blockedActionCache.save(blockedActionData);
        }

        return blockedActionData;
    }

    @Override
    public List<BlockedActionData> getAll() {

        List<BlockedActionData> blockedActionDataList = this.blockedActionCache.getAll();

        if (blockedActionDataList.isEmpty()) {
            List<BlockedAction> blockedActions = new ArrayList<>();

            for (var blockedAction : this.blockedActionRepository.findAll()) {

                BlockedActionData blockedActionData=new BlockedActionData(blockedAction.getBlockedActionId(),
                                                                          blockedAction.getUser()
                                                                                       .getId(),
                                                                          blockedAction.getIAMAction()
                                                                                       .getActionId());
            }
        }

        return blockedActionDataList;

    }

}