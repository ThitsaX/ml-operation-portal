package com.thitsaworks.operation_portal.core.test_iam.cache.redis;

import com.thitsaworks.operation_portal.component.common.identifier.ActionId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.GrantId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.RoleId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.test_iam.cache.RoleGrantCache;
import com.thitsaworks.operation_portal.core.test_iam.data.RoleGrantData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.RoleGrantRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier(CacheQualifiers.REDIS)
@RequiredArgsConstructor
public class RedisRoleGrantCache implements RoleGrantCache {
    private static final String WITH_ID = "rd_role_grant_id";

    private static final String ROLE_ID = "rd_role_id";

    private static final String ACTION_ID = "rd_action_id";

    private final RoleGrantRepository roleGrantRepository;

    private final RedissonClient redissonClient;

    @Override
    public void save(RoleGrantData roleGrantData) {
        RMapCache<GrantId, RoleGrantData> withId = this.redissonClient.getMapCache(WITH_ID);
        RMapCache<RoleId, RoleGrantData> withRoleId = this.redissonClient.getMapCache(ROLE_ID);
        RMapCache<ActionId, RoleGrantData> withActionId = this.redissonClient.getMapCache(ACTION_ID);

        var deleted = withId.remove(roleGrantData.grantId());

        if (deleted != null) {
            withRoleId.remove(deleted.roleId());
            withActionId.remove(deleted.actionId());
        }

        withId.put(roleGrantData.grantId(), roleGrantData);
        withRoleId.put(roleGrantData.roleId(), roleGrantData);
        withActionId.put(roleGrantData.actionId(), roleGrantData);

    }
    @Override
    public void delete(GrantId grantId) {
        RMapCache<GrantId, RoleGrantData> withId = this.redissonClient.getMapCache(WITH_ID);
        RMapCache<RoleId, RoleGrantData> withRoleId = this.redissonClient.getMapCache(ROLE_ID);
        RMapCache<ActionId, RoleGrantData> withActionId = this.redissonClient.getMapCache(ACTION_ID);

        var deleted = withId.remove(grantId);

        if (deleted != null) {
            withRoleId.remove(deleted.roleId());
            withActionId.remove(deleted.actionId());
        }

    }

    @Override
    public Optional<RoleGrantData> find(GrantId grantId) throws IAMException {
        RMapCache<GrantId, RoleGrantData> withId = this.redissonClient.getMapCache(WITH_ID);

        var roleGrantData = withId.get(grantId);

        var roleGrant =this.roleGrantRepository.findById(grantId)
                                               .orElseThrow(() -> new IAMException(IAMErrors.ROLE_NOT_FOUND));

        if( roleGrantData == null && roleGrant != null) {
            roleGrantData = new RoleGrantData(roleGrant.getGrantId(),
                                              roleGrant.getRole().getRoleId(),
                                              roleGrant.getIAMAction().getActionId());
            save(roleGrantData);
        }
        else {
            return Optional.empty();
        }

        return Optional.of(roleGrantData);
    }

    @Override
    public RoleGrantData get(GrantId grantId) throws IAMException {
        RMapCache<GrantId, RoleGrantData> withId = this.redissonClient.getMapCache(WITH_ID);

        RoleGrantData roleGrantData = withId.get(grantId);

        var roleGrant = this.roleGrantRepository.findById(grantId)
                                                .orElseThrow(() -> new IAMException(IAMErrors.BLOCKED_ACTION_NOT_FOUND));

        if (roleGrantData == null && roleGrant != null) {
            roleGrantData = new RoleGrantData(roleGrant.getGrantId(),
                                              roleGrant.getRole().getRoleId(),
                                              roleGrant.getIAMAction().getActionId());
            this.save(roleGrantData);
        }

        return roleGrantData;
    }

    @Override
    public List<RoleGrantData> getAll() {

        RMapCache<GrantId, RoleGrantData> withId = this.redissonClient.getMapCache(WITH_ID);

        List<RoleGrantData> roleGrantDataList = new ArrayList<>();
        for (RoleGrantData roleGrantData : withId.values()) {
            if (roleGrantData != null) {
                roleGrantDataList.add(roleGrantData);
            }
        }
        return roleGrantDataList;
    }
    @PostConstruct
    private void postConstruct() {
        for (var roleGrant : this.roleGrantRepository.findAll()) {
            RoleGrantData roleGrantData = new RoleGrantData(roleGrant.getGrantId(),
                                                            roleGrant.getRole().getRoleId(),
                                                            roleGrant.getIAMAction().getActionId());
            this.save(roleGrantData);
        }
    }


}
