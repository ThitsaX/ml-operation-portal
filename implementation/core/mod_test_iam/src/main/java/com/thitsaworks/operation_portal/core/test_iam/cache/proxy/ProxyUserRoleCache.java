package com.thitsaworks.operation_portal.core.test_iam.cache.proxy;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.UserRoleId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.test_iam.cache.UserRoleCache;
import com.thitsaworks.operation_portal.core.test_iam.data.UserRoleData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.UserRole;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.UserRoleRepository;
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
public class ProxyUserRoleCache implements UserRoleCache {

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    @Qualifier(CacheQualifiers.REDIS)
    private UserRoleCache userRoleCache;

    @Override
    public void save(UserRoleData userRoleData) {
        this.userRoleCache.save(userRoleData);
    }

    @Override
    public void delete(UserRoleId userRoleId) {
        this.userRoleCache.delete(userRoleId);
    }

    @Override
    public Optional<UserRoleData> find(UserRoleId userRoleId) throws IAMException {

        Optional<UserRoleData> userRoleData = this.userRoleCache.find(userRoleId);

        if (userRoleData.isEmpty()) {
            var userRole = this.userRoleRepository.findById(userRoleId)
                                                  .orElseThrow(() -> new IAMException(IAMErrors.USER_ROLE_NOT_FOUND));

            userRoleData = Optional.of(new UserRoleData(userRole.getUserRoleId(),
                                                        userRole.getRole()
                                                                .getRoleId(),
                                                        userRole.getUser()
                                                                .getUserId()));
            this.userRoleCache.save(userRoleData.get());
        }

        return userRoleData;
    }

    @Override
    public UserRoleData get(UserRoleId userRoleId) throws IAMException {

        UserRoleData userRoleData = this.userRoleCache.get(userRoleId);

        if(userRoleData == null) {
            var userRole = this.userRoleRepository.findById(userRoleId)
                                                  .orElseThrow(() -> new IAMException(IAMErrors.USER_ROLE_NOT_FOUND));
            userRoleData = new UserRoleData(userRole.getUserRoleId(),
                                            userRole.getRole()
                                                    .getRoleId(),
                                            userRole.getUser()
                                                    .getUserId());
            this.userRoleCache.save(userRoleData);
        }

        return userRoleData;

    }

    @Override
    public List<UserRoleData> getAll() {

        List<UserRoleData> userRoleDataList = this.userRoleCache.getAll();

        if(userRoleDataList.isEmpty()) {
            List<UserRoleData> list = new ArrayList<>();
            for (UserRole userRole : this.userRoleRepository.findAll()) {
                UserRoleData userRoleData = new UserRoleData(userRole.getUserRoleId(),
                                                             userRole.getRole()
                                                                     .getRoleId(),
                                                             userRole.getUser()
                                                                     .getUserId());
                list.add(userRoleData);
            }
            userRoleDataList = list;
        }
        return userRoleDataList;
    }

}
