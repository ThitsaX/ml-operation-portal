package com.thitsaworks.operation_portal.core.iam.model.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.AccessKey;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.identifier.RealmId;
import com.thitsaworks.operation_portal.component.common.type.RealmType;
import com.thitsaworks.operation_portal.core.iam.model.Principal;
import com.thitsaworks.operation_portal.core.iam.model.QPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrincipalRepository
        extends JpaRepository<Principal, PrincipalId>, QuerydslPredicateExecutor<Principal> {

    public Optional<Principal> findByPrincipalId(PrincipalId principalId);

    class Filters {

        public static BooleanExpression withPrincipalId(PrincipalId principalId) {

            return QPrincipal.principal.principalId.eq(principalId);
        }

        public static BooleanExpression withRealm(RealmType realmType) {

            return QPrincipal.principal.realmType.eq(realmType);
        }

        public static BooleanExpression withAccessKey(AccessKey accessKey) {

            return QPrincipal.principal.accessKey.eq(accessKey);
        }

        public static BooleanExpression withRealmId(RealmId realmId) {

            return QPrincipal.principal.realmId.eq(realmId);
        }

    }

}
