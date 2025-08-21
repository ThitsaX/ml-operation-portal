package com.thitsaworks.operation_portal.core.iam.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalRoleData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.PrincipalRole;
import com.thitsaworks.operation_portal.core.iam.model.QPrincipalRole;
import com.thitsaworks.operation_portal.core.iam.model.repository.PrincipalRoleRepository;
import com.thitsaworks.operation_portal.core.iam.query.PrincipalRoleQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrincipalRoleJpaQueryHandler implements PrincipalRoleQuery {

    private final QPrincipalRole principalRole = QPrincipalRole.principalRole;


    private final PrincipalRoleRepository principalRoleRepository;

    @Override
    public List<PrincipalRoleData> getRoles(PrincipalId principalId) throws IAMException {

        BooleanExpression expression = principalRole.principal.principalId.eq(principalId);
        List<PrincipalRole> principalRoles = (List<PrincipalRole>) principalRoleRepository.findAll(expression);
        return principalRoles.stream().map(PrincipalRoleData::new).collect(Collectors.toList());
    }

}
