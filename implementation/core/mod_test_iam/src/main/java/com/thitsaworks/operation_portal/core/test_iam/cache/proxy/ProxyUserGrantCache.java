package com.thitsaworks.operation_portal.core.test_iam.cache.proxy;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.GrantId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.test_iam.cache.UserGrantCache;
import com.thitsaworks.operation_portal.core.test_iam.data.UserGrantData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.UserGrant;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.UserGrantRepository;
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
public class ProxyUserGrantCache implements UserGrantCache {

    @Autowired
    private UserGrantRepository userGrantRepository;

    @Autowired
    @Qualifier(CacheQualifiers.REDIS)
    private UserGrantCache userGrantCache;

    @Override
    public void save(UserGrantData userGrantData) {

        this.userGrantCache.save(userGrantData);
    }

    @Override
    public void delete(GrantId grantId) {

        this.userGrantCache.delete(grantId);
    }

    @Override
    public Optional<UserGrantData> find(GrantId grantId) throws IAMException {
        Optional<UserGrantData> userGrantData = this.userGrantCache.find(grantId);

        if (userGrantData.isEmpty()) {
            var userGrant = this.userGrantRepository.findById(grantId)
                                                    .orElseThrow(() -> new IAMException(IAMErrors.USER_GRANT_NOT_FOUND));

            userGrantData = Optional.of(new UserGrantData(userGrant.getGrantId(),
                                                          userGrant.getUser()
                                                                   .getUserId(),
                                                          userGrant.getIAMAction()
                                                                   .getActionId()));
            this.userGrantCache.save(userGrantData.get());
        }

        return userGrantData;
    }

    @Override
    public UserGrantData get(GrantId grantId) throws IAMException {
        UserGrantData userGrantData = this.userGrantCache.get(grantId);

        if (userGrantData == null) {
            Optional<UserGrant> optionalUserGrant =
                this.userGrantRepository.findById(grantId);
            if (optionalUserGrant.isPresent()) {
                UserGrant userGrant = optionalUserGrant.get();
                userGrantData = new UserGrantData(userGrant.getGrantId(),
                                                  userGrant.getUser()
                                                           .getUserId(),
                                                  userGrant.getIAMAction()
                                                           .getActionId());
                this.userGrantCache.save(userGrantData);
            }
        }

        return userGrantData;

    }

    @Override
    public List<UserGrantData> getAll() {

        List<UserGrantData> userGrants = this.userGrantCache.getAll();

        if (userGrants.isEmpty()) {
            List<UserGrantData> list = new ArrayList<>();
            for (UserGrant userGrant : this.userGrantRepository.findAll()) {
                UserGrantData data = new UserGrantData(userGrant.getGrantId(),
                                                       userGrant.getUser()
                                                                .getUserId(),
                                                       userGrant.getIAMAction()
                                                                .getActionId());
                list.add(data);
            }
            userGrants = list;
        }

        return userGrants;
    }

}