package com.thitsaworks.operation_portal.core.test_iam.cache.redis;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.RoleId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.test_iam.cache.RoleCache;
import com.thitsaworks.operation_portal.core.test_iam.data.RoleData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Qualifier(CacheQualifiers.REDIS)
@RequiredArgsConstructor
public class RedisRoleCache implements RoleCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedisRoleCache.class);

    private static final String WITH_ID = "rd_role_with_id";

    private static final String WITH_NAME = "rd_role_with_name";

    private static RoleRepository roleRepository;

    private final RedissonClient redissonClient;



    @Override
    public void save(RoleData roleData) {
        RMapCache<RoleId, RoleData> withId = this.redissonClient.getMapCache(WITH_ID);
        RMapCache<String, RoleData> withName = this.redissonClient.getMapCache(WITH_NAME);

        var deleted = withId.remove(roleData.roleId());

        if (deleted != null) {
            withName.remove(deleted.name());
        }

        withId.put(roleData.roleId(), roleData);
        withName.put(roleData.name(), roleData);

    }

    @Override
    public void delete(RoleId roleId) {
        RMapCache<RoleId, RoleData> withId = this.redissonClient.getMapCache(WITH_ID);
        RMapCache<String, RoleData> withName = this.redissonClient.getMapCache(WITH_NAME);

        var deleted = withId.remove(roleId);

        if (deleted != null) {
            withName.remove(deleted.name());
        }

    }

    @Override
    public Optional<RoleData> find(RoleId roleId) throws IAMException {

        RMapCache<RoleId, RoleData> withId = this.redissonClient.getMapCache(WITH_ID);

        var roleData = withId.get(roleId);

        var role = roleRepository.findById(roleId)
                                 .orElseThrow(() -> new IAMException(IAMErrors.ROLE_NOT_FOUND));

        if (roleData == null && role != null) {
            roleData = new RoleData(role.getRoleId(),
                                    role.getName(),
                                    role.getActive());
            this.save(roleData);
        } else {
            return Optional.empty();
        }

        return Optional.of(roleData);
    }

    @Override
    public RoleData get(RoleId roleId) throws IAMException {
        RMapCache<RoleId, RoleData> withId = this.redissonClient.getMapCache(WITH_ID);

        RoleData roleData = withId.get(roleId);

        var role = roleRepository.findById(roleId)
                                 .orElseThrow(() -> new IAMException(IAMErrors.ROLE_NOT_FOUND));
        if (roleData == null && role != null) {
            roleData = new RoleData(role.getRoleId(),
                                    role.getName(),
                                    role.getActive());
            this.save(roleData);
        }
        return roleData;

    }


    @Override
    public RoleData get(String name) throws IAMException {
        RMapCache<String, RoleData> withName = this.redissonClient.getMapCache(WITH_NAME);

        RoleData roleData = withName.get(name);

        var role = roleRepository.findOne(RoleRepository.Filters.withName(name))
                                 .orElseThrow(() -> new IAMException(IAMErrors.ROLE_NOT_FOUND));
        if (roleData == null && role != null) {
            roleData = new RoleData(role.getRoleId(),
                                    role.getName(),
                                    role.getActive());
            this.save(roleData);
        }
        return roleData;
    }

    @Override
    public List<RoleData> getAll() {

        List<RoleData> dataList = new ArrayList<>();
        RMapCache<RoleId, RoleData> withId = this.redissonClient.getMapCache(WITH_ID);

        withId.forEach((roleId, roleData) -> {
            if (roleData != null) {
                dataList.add(roleData);
            }
        });

        return dataList;

    }
    @PostConstruct
    private void postConstruct(){

        for (var role : roleRepository.findAll()) {
            RoleData roleData = new RoleData(role.getRoleId(),
                                             role.getName(),
                                             role.getActive());
            this.save(roleData);
        }
    }

}
