package com.thitsaworks.operation_portal.usecase.hub_operator.impl;

import com.thitsaworks.operation_portal.datasource.persistence.WriteTransactional;
import com.thitsaworks.operation_portal.hubuser.domain.command.RemoveAnnouncements;
import com.thitsaworks.operation_portal.usecase.hub_operator.RemovePastSixMonthsAnnouncements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemovePastSixMonthsAnnouncementsBean extends RemovePastSixMonthsAnnouncements {

    private static final Logger LOG = LoggerFactory.getLogger(RemovePastSixMonthsAnnouncementsBean.class);

    @Autowired
    private RemoveAnnouncements removeAnnouncements;

    @Override
    @WriteTransactional
    public RemovePastSixMonthsAnnouncements.Output onExecute(RemovePastSixMonthsAnnouncements.Input input)
            throws Exception {

        RemoveAnnouncements.Output output = this.removeAnnouncements.execute(
                new RemoveAnnouncements.Input());

        return new RemovePastSixMonthsAnnouncements.Output(output.isRemoved());
    }

    @Override
    protected String getName() {

        return RemovePastSixMonthsAnnouncements.class.getCanonicalName();
    }

    @Override
    protected String getDescription() {

        return null;
    }

    @Override
    protected String getScope() {

        return "uc_hub_operator";
    }

    @Override
    protected String getId() {

        return RemovePastSixMonthsAnnouncements.class.getName();
    }

    @Override
    public boolean isOwned(Object userDetails) {

        return true;
    }

    @Override
    public boolean isAuthorized(Object userDetails) {

        return true;
    }

}
