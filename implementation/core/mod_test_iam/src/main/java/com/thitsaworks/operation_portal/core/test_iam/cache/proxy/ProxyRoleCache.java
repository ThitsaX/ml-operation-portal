package com.thitsaworks.operation_portal.core.test_iam.cache.proxy;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.RoleId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.test_iam.cache.RoleCache;
import com.thitsaworks.operation_portal.core.test_iam.data.RoleData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.Role;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.RoleRepository;
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
public class ProxyRoleCache implements RoleCache {


    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    @Qualifier(CacheQualifiers.REDIS)
    private RoleCache roleCache;

    @Override
    public void save(RoleData roleData) {
        this.roleCache.save(roleData);
    }

    @Override
    public void delete(RoleId roleId) {

        this.roleCache.delete(roleId);
    }

    @Override
    public Optional<RoleData> find(RoleId roleId) throws IAMException {
        Optional<RoleData> roleData = this.roleCache.find(roleId);

        var role = this.roleRepository.findById(roleId).orElseThrow(() -> new IAMException(IAMErrors.ROLE_NOT_FOUND));
        if (roleData.isEmpty()) {
            roleData = Optional.of(new RoleData(role.getRoleId(),
                                                role.getName(),
                                                role.getActive()));
            this.roleCache.save(roleData.get());
        }
        return roleData;
    }

    @Override
    public RoleData get(RoleId roleId) throws IAMException {

        RoleData roleData = this.roleCache.get(roleId);
        if (roleData == null) {
            var role = this.roleRepository.findById(roleId).orElseThrow(() -> new IAMException(IAMErrors.ROLE_NOT_FOUND));
            roleData = new RoleData(role
                                        .getRoleId(),
                                    role.getName(),
                                    role.getActive());
            this.roleCache.save(roleData);
        }
        return roleData;
    }

    @Override
    public RoleData get(String name) throws IAMException {
        RoleData roleData = this.roleCache.get(name);
        if (roleData == null) {
            var role = this.roleRepository.findOne(RoleRepository.Filters.withName(name))
                                          .orElseThrow(() -> new IAMException(IAMErrors.ROLE_NOT_FOUND));
            roleData = new RoleData(role
                                        .getRoleId(),
                                    role.getName(),
                                    role.getActive());
            this.roleCache.save(roleData);
        }
        return roleData;
    }

    @Override
    public List<RoleData> getAll() {
        List<RoleData> roleDataList = this.roleCache.getAll();
        if (roleDataList.isEmpty()) {
            List<RoleData> roles= new ArrayList<>();
            for (Role role : this.roleRepository.findAll()) {
                RoleData roleData = new RoleData(role.getRoleId(),
                                                 role.getName(),
                                                 role.getActive());
                roles.add(roleData);
            }
            roleDataList = roles;
        }
        return roleDataList;
    }

}
