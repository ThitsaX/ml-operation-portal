package com.thitsaworks.operation_portal.iam.domain.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.iam.domain.Principal;
import com.thitsaworks.operation_portal.iam.identity.AccessKey;
import com.thitsaworks.operation_portal.iam.identity.PrincipalId;
import com.thitsaworks.operation_portal.iam.identity.RealmId;
import com.thitsaworks.operation_portal.iam.type.PrincipalStatus;
import com.thitsaworks.operation_portal.iam.type.RealmType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrincipalRepository
        extends JpaRepository<Principal, PrincipalId>, QuerydslPredicateExecutor<Principal>,
        CrudRepository<Principal,PrincipalId> {

    Optional<Principal> findByPrincipalIdAndStatus(PrincipalId principalId, PrincipalStatus status);

    class Filters {

        public static BooleanExpression withPrincipalId(PrincipalId principalId) {

            //return QPrincipal.principal.principalId.eq(principalId);
            return null;
        }

        public static BooleanExpression withRealm(RealmType realmType) {

           // return QPrincipal.principal.realmType.eq(realmType);
            return null;
        }

        public static BooleanExpression withAccessKey(AccessKey accessKey) {

           // return QPrincipal.principal.accessKey.eq(accessKey);
            return null;
        }

        public static BooleanExpression withRealmId(RealmId realmId) {

           // return QPrincipal.principal.realmId.eq(realmId);
            return null;
        }

    }

}
