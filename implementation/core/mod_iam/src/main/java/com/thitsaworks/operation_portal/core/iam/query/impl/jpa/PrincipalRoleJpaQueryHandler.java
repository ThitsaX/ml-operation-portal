package com.thitsaworks.operation_portal.core.iam.query.impl.jpa;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.core.iam.data.PrincipalRoleData;
import com.thitsaworks.operation_portal.core.iam.exception.IAMErrors;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.model.QPrincipalRole;
import com.thitsaworks.operation_portal.core.iam.model.repository.PrincipalRoleRepository;
import com.thitsaworks.operation_portal.core.iam.query.PrincipalRoleQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalRoleJpaQueryHandler implements PrincipalRoleQuery {

    private final QPrincipalRole principalRole = QPrincipalRole.principalRole;

    private final PrincipalRoleRepository principalRoleRepository;

    @Override
    public PrincipalRoleData getRole(PrincipalId principalId) throws IAMException {

        BooleanExpression predicate = this.principalRole.principal.principalId.eq(principalId);

        var
            principalRole =
            this.principalRoleRepository.findOne(predicate)
                                        .orElseThrow(() -> new IAMException(IAMErrors.ROLE_NOT_FOUND));

        return new PrincipalRoleData(principalRole);
    }

}
