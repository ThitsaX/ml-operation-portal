package com.thitsaworks.operation_portal.core.test_iam.cache;

import com.thitsaworks.operation_portal.component.common.identifier.UserId;
import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.RoleId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import com.thitsaworks.operation_portal.core.test_iam.data.RoleData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.Role;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;

import java.util.List;
import java.util.Optional;

public interface RoleCache {
    void  save(RoleData roleData);

    void delete(RoleId roleId);

    Optional<RoleData> find(RoleId roleId) throws IAMException;

    RoleData get(RoleId roleId) throws IAMException;

    RoleData get(String name) throws IAMException;

    List<RoleData> getAll();

    class Updater{

        @PostPersist
        @PostUpdate
        public void persistOrUpdate(Role role){

            RoleCache roleCache= SpringContext.getBean(RoleCache.class, CacheQualifiers.DEFAULT);

            RoleData roleData =new RoleData(role.getRoleId(),
                                            role.getName(),
                                            role.getActive());

            roleCache.save(roleData);
        }

        @PostRemove
        public void postRemove(Role role){
            RoleCache roleCache= SpringContext.getBean(RoleCache.class,CacheQualifiers.DEFAULT);

            roleCache.delete(role.getRoleId());
        }
    }


}
