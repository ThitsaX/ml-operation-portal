package com.thitsaworks.operation_portal.usecase.util.action;

import com.thitsaworks.operation_portal.component.common.identifier.PrincipalId;
import com.thitsaworks.operation_portal.component.common.type.ActionCode;
import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;
import com.thitsaworks.operation_portal.core.iam.command.CreateOrUpdateActionCommand;
import com.thitsaworks.operation_portal.core.iam.data.ActionData;
import com.thitsaworks.operation_portal.core.iam.engine.IAMEngine;
import com.thitsaworks.operation_portal.core.iam.exception.IAMException;
import com.thitsaworks.operation_portal.core.iam.query.ActionQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.scheduler.ScheduledJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class ActionAuthorizationManager {

    private final CreateOrUpdateActionCommand createOrUpdateActionCommand;

    private final IAMEngine iamEngine;

    private final ActionQuery actionQuery;

    @Autowired
    public ActionAuthorizationManager(CreateOrUpdateActionCommand createOrUpdateActionCommand,
                                      IAMEngine iamEngine,
                                      ActionQuery actionQuery) {

        this.createOrUpdateActionCommand = createOrUpdateActionCommand;
        this.iamEngine = iamEngine;
        this.actionQuery = actionQuery;
    }

    public void registerAction(String actionName, String scope, String description) {

        var input = new CreateOrUpdateActionCommand.Input(new ActionCode(actionName),
                                                          scope,
                                                          description);

        this.createOrUpdateActionCommand.execute(input);
    }

    public boolean isAuthorizedTo(PrincipalId principalId, ActionCode actionCode) throws IAMException {

        return this.iamEngine.isGrantedAction(principalId, actionCode);
    }

    public ActionData getAction(ActionCode actionCode) throws IAMException {

        return this.actionQuery.get(actionCode);
    }

    public List<String> findAuditableActions() throws IAMException {

        try {

            String[] packages = {"com.thitsaworks.operation_portal.usecase.operation_portal"};

            List<String> auditableActionNames = new ArrayList<>();
            ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

            scanner.addIncludeFilter(new AssignableTypeFilter(OperationPortalAuditableUseCase.class));
            scanner.addIncludeFilter(new AssignableTypeFilter(ScheduledJob.class));

            Set<String> ignoredBaseClasses = Set.of(OperationPortalAuditableUseCase.class.getName(),
                                                    ScheduledJob.class.getName());

            for (String pkg : packages) {
                for (BeanDefinition bd : scanner.findCandidateComponents(pkg)) {
                    String className = bd.getBeanClassName();
                    if (className != null && !ignoredBaseClasses.contains(className)) {
                        String simpleName = className.substring(className.lastIndexOf('.') + 1);
                        String actionName = simpleName.replaceFirst("(Handler|UseCase)$", "");
                        auditableActionNames.add(actionName);
                    }
                }
            }

            return auditableActionNames;

        } catch (Exception e) {
            throw new IAMException(new ErrorMessage(e.getMessage(), e.getMessage()));
        }
    }

}
