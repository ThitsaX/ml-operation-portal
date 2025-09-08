package com.thitsaworks.operation_portal.core.participant.cache.proxy;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.participant.cache.UserCache;
import com.thitsaworks.operation_portal.core.participant.data.UserData;
import com.thitsaworks.operation_portal.core.participant.model.User;
import com.thitsaworks.operation_portal.core.participant.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

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
    public UserData get(UserId userId) {

        UserData userData = this.userCache.get(userId);

        if (userData == null) {

            Optional<User> optionalUser = this.userRepository.findById(
                    userId);

            if (optionalUser.isEmpty()) {

                return null;
            }

            userData = new UserData(optionalUser.get());

            this.userCache.save(userData);
        }

        return userData;
    }

    @Override
    public void delete(UserId userId) {

        this.userCache.delete(userId);

    }

}
