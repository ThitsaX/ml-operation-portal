package com.thitsaworks.operation_portal.core.test_iam.model.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.core.test_iam.model.QUser;
import com.thitsaworks.operation_portal.core.test_iam.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UserId>, QuerydslPredicateExecutor<User> {


    public Optional<User> findByUserId(UserId userId);

    class Filters {

        public static BooleanExpression withUserId(UserId userId) {

            return QUser.user.userId.eq(userId);
        }

        public static BooleanExpression withRealm(RealmType realmType) {

            return QUser.user.realmType.eq(realmType);
        }

        public static BooleanExpression withAccessKey(AccessKey accessKey) {

            return QUser.user.accessKey.eq(accessKey);
        }

        public static BooleanExpression withRealmId(RealmId realmId) {

            return QUser.user.realmId.eq(realmId);
        }

    }

}
