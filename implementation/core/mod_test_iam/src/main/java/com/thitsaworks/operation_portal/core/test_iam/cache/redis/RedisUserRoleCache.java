package com.thitsaworks.operation_portal.core.test_iam.cache.redis;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.RoleId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.UserRoleId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.test_iam.cache.UserRoleCache;
import com.thitsaworks.operation_portal.core.test_iam.data.UserRoleData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.UserRoleRepository;
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
public class RedisUserRoleCache implements UserRoleCache {
    private static final String USER_ROLE_ID = "rd_user_role_id";
    private static final String USER_ID = "rd_user_id";
    private static final String ROLE_ID = "rd_role_id";

    private final UserRoleRepository userRoleRepository;
    private final RedissonClient redissonClient;

    @Override
    public void save(UserRoleData userRoleData) {
        RMapCache<UserRoleId, UserRoleData> withUserRoleId = this.redissonClient.getMapCache(USER_ROLE_ID);
        RMapCache<UserId, UserRoleData> withUserId = this.redissonClient.getMapCache(USER_ID);
        RMapCache<RoleId, UserRoleData> withRoleId = this.redissonClient.getMapCache(ROLE_ID);

        var deleted = withUserRoleId.remove(userRoleData.userRoleId());

        if (deleted != null) {
            withUserId.remove(deleted.userId());
            withRoleId.remove(deleted.roleId());
        }

        withUserRoleId.put(userRoleData.userRoleId(), userRoleData);
        withUserId.put(userRoleData.userId(), userRoleData);
        withRoleId.put(userRoleData.roleId(), userRoleData);

    }

    @Override
    public void delete(UserRoleId userRoleId) {
        RMapCache<UserRoleId, UserRoleData> withUserRoleId = this.redissonClient.getMapCache(USER_ROLE_ID);
        RMapCache<UserId, UserRoleData> withUserId = this.redissonClient.getMapCache(USER_ID);
        RMapCache<RoleId, UserRoleData> withRoleId = this.redissonClient.getMapCache(ROLE_ID);

        var deleted = withUserRoleId.remove(userRoleId);

        if (deleted != null) {
            withUserId.remove(deleted.userId());
            withRoleId.remove(deleted.roleId());
        }

    }

    @Override
    public Optional<UserRoleData> find(UserRoleId userRoleId) throws IAMException {
        RMapCache<UserRoleId, UserRoleData> withUserRoleId = this.redissonClient.getMapCache(USER_ROLE_ID);

        var userRoleData = withUserRoleId.get(userRoleId);

        var userrole= this.userRoleRepository.findById(userRoleId)
                                             .orElseThrow(() -> new IAMException(IAMErrors.USER_ROLE_NOT_FOUND));
        if (userRoleData == null && userrole != null) {

            userRoleData = new UserRoleData(userrole.getUserRoleId(),
                                            userrole.getRole().getRoleId(),
                                            userrole.getUser().getUserId());

            this.save(userRoleData);
        } else {
            return Optional.empty();
        }
        return Optional.of(userRoleData);
    }

    @Override
    public UserRoleData get(UserRoleId userRoleId) {
        RMapCache<UserRoleId, UserRoleData> withUserRoleId = this.redissonClient.getMapCache(USER_ROLE_ID);

        UserRoleData userRoleData = withUserRoleId.get(userRoleId);

        var userrole = this.userRoleRepository.findById(userRoleId)
                                              .orElseThrow(() -> new RuntimeException("UserRole not found"));

        if (userRoleData == null && userrole != null) {

            userRoleData = new UserRoleData(userrole.getUserRoleId(),
                                            userrole.getRole().getRoleId(),
                                            userrole.getUser().getUserId());

            this.save(userRoleData);
        } else {
            return null;
        }
        return userRoleData;

    }


    @Override
    public List<UserRoleData> getAll() {
        RMapCache<UserRoleId, UserRoleData> withUserRoleId = this.redissonClient.getMapCache(USER_ROLE_ID);

        List<UserRoleData> userRoleDataList = new ArrayList<>();

        for (UserRoleData userRoleData : withUserRoleId.values()) {
            if (userRoleData != null) {
                userRoleDataList.add(userRoleData);
            }
        }
        return userRoleDataList;
    }

    @PostConstruct
    private void postConstruct() {

        for(var userRole : this.userRoleRepository.findAll()) {
            UserRoleData userRoleData = new UserRoleData(userRole.getUserRoleId(),
                                                         userRole.getRole().getRoleId(),
                                                         userRole.getUser().getUserId());
            this.save(userRoleData);
        }
    }

}
