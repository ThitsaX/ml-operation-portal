package com.thitsaworks.operation_portal.core.test_iam.cache.proxy;

import com.thitsaworks.operation_portal.component.common.identifier.iamtestid.GrantId;
import com.thitsaworks.operation_portal.component.misc.spring.CacheQualifiers;
import com.thitsaworks.operation_portal.core.test_iam.cache.RoleGrantCache;
import com.thitsaworks.operation_portal.core.test_iam.data.RoleGrantData;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.test_iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.test_iam.model.RoleGrant;
import com.thitsaworks.operation_portal.core.test_iam.model.repository.RoleGrantRepository;
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
public class ProxyRoleGrantCache implements RoleGrantCache {

    @Autowired
    private RoleGrantRepository roleGrantRepository;

    @Autowired
    @Qualifier(CacheQualifiers.REDIS)
    private RoleGrantCache roleGrantCache;

    @Override
    public void save(RoleGrantData roleGrantData) {

        this.roleGrantCache.save(roleGrantData);
    }

    @Override
    public void delete(GrantId grantId) {

        this.roleGrantCache.delete(grantId);
    }

    @Override
    public Optional<RoleGrantData> find(GrantId grantId) throws IAMException {

        Optional<RoleGrantData> roleGrantData = this.roleGrantCache.find(grantId);
        if (roleGrantData.isEmpty()) {
            var roleGrant = this.roleGrantRepository.findById(grantId)
                                                    .orElseThrow(() -> new IAMException(IAMErrors.ROLE_GRANT_NOT_FOUND));

            roleGrantData = Optional.of(new RoleGrantData(roleGrant.getGrantId(),
                                                          roleGrant.getRole()
                                                                   .getRoleId(),
                                                          roleGrant.getIAMAction()
                                                                   .getActionId()));
            this.roleGrantCache.save(roleGrantData.get());
        }

        return roleGrantData;
    }

    @Override
    public RoleGrantData get(GrantId grantId) throws IAMException {

        RoleGrantData roleGrantData = this.roleGrantCache.get(grantId);

        if (roleGrantData == null) {
            Optional<RoleGrant> optionalRoleGrant =
                this.roleGrantRepository.findById(grantId);
            if (optionalRoleGrant.isPresent()) {
                RoleGrant roleGrant = optionalRoleGrant.get();
                roleGrantData = new RoleGrantData(roleGrant.getGrantId(),
                                                  roleGrant.getRole()
                                                           .getRoleId(),
                                                  roleGrant.getIAMAction()
                                                           .getActionId());
                this.roleGrantCache.save(roleGrantData);
            }
        }

        return roleGrantData;

    }

    @Override
    public List<RoleGrantData> getAll() {

        List<RoleGrantData> roleGrants = this.roleGrantCache.getAll();
        if (roleGrants.isEmpty()) {
            List<RoleGrant> roleGrantList = new ArrayList<>();
            for (
                RoleGrant roleGrant : this.roleGrantRepository.findAll()) {
                RoleGrantData roleGrantData = new RoleGrantData(roleGrant.getGrantId(),
                                                                roleGrant.getRole()
                                                                         .getRoleId(),
                                                                roleGrant.getIAMAction()
                                                                         .getActionId());
                roleGrants.add(roleGrantData);
                this.roleGrantCache.save(roleGrantData);
            }
        }
        return roleGrants;
    }

}