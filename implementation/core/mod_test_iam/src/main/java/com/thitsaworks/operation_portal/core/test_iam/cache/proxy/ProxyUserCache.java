package com.thitsaworks.operation_portal.core.test_iam.cache.proxy;


import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.test_iam.cache.UserCache;
import com.thitsaworks.operation_portal.core.test_iam.data.UserData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.User;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.UserRepository;
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
public class ProxyUserCache implements UserCache {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier(CacheQualifiers.REDIS)
    private UserCache userCache;

    @Override
    public void save(UserData userData) {
        this.userCache.save(userData);

    }

    @Override
    public void delete(UserId id) {
        this.userCache.delete(id);

    }

    @Override
    public Optional<UserData> find(AccessKey accessKey) throws IAMException {
        Optional<UserData> userData = this.userCache.find(accessKey);

        var user = this.userRepository.findOne(UserRepository.Filters.withAccessKey(accessKey)).orElseThrow(() -> new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND));

        if (user == null) {
            return Optional.empty();
        }

        if (userData.isEmpty()) {
            userData = Optional.of(new UserData(user));
            this.userCache.save(userData.get());
        }

        return userData;
    }


    @Override
    public UserData get(UserId id) throws IAMException {
        UserData userData = this.userCache.get(id);

        if (userData == null) {
            Optional<User> optionalUser = this.userRepository.findOne(UserRepository.Filters.withUserId(id));

            if (optionalUser.isEmpty()) {
                throw new IAMException(IAMErrors.PRINCIPAL_NOT_FOUND);
            }

            userData = new UserData(optionalUser.get());
            this.userCache.save(userData);
        }

        return userData;
    }

    @Override
    public UserData get(AccessKey accessKey, RealmType realmType) throws IAMException {
        UserData userData = this.userCache.get(accessKey, realmType);

        if (userData == null) {
            Optional<User> optionalUser = this.userRepository.findOne(
                UserRepository.Filters.withAccessKey(accessKey)
                                      .and(UserRepository.Filters.withRealm(realmType)));

            if (optionalUser.isEmpty()) {
                return null;
            }

            userData = new UserData(optionalUser.get());
            this.userCache.save(userData);
        }

        return userData;
    }

    @Override
    public List<UserData> getAll() {
        List<UserData> userData = this.userCache.getAll();

        if (userData.isEmpty()) {
            List<UserData> list = new ArrayList<>();
            for (User user : this.userRepository.findAll()) {
                UserData data = new UserData(user.getUserId(),
                                             user.getAccessKey(),
                                             user.getSecretKey(),
                                             user.getRealmType(),
                                             user.getRealmId(),
                                             user.getPrincipalStatus());
                list.add(data);
            }
            userData = list;
        }

        return userData;
    }

    @Override
    public void delete(AccessKey accessKey) {
        this.userCache.delete(accessKey);

    }

}