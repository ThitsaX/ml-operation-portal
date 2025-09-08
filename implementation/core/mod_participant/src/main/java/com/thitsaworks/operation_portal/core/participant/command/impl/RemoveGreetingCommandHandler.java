package com.thitsaworks.operation_portal.core.participant.command.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.thitsaworks.operation_portal.component.common.identifier.GreetingId;
import com.thitsaworks.operation_portal.component.misc.persistence.transactional.CoreWriteTransactional;
import com.thitsaworks.operation_portal.core.participant.command.RemoveGreetingCommand;
import com.thitsaworks.operation_portal.core.participant.model.GreetingMessage;
import com.thitsaworks.operation_portal.core.participant.model.QGreetingMessage;
import com.thitsaworks.operation_portal.core.participant.model.repository.GreetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RemoveGreetingCommandHandler implements RemoveGreetingCommand {
    private final GreetingRepository greetingRepository;

    private final QGreetingMessage greetingMessage= QGreetingMessage.greetingMessage;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @CoreWriteTransactional
    public Output execute(Input input) {
        LocalDate currentDate = LocalDate.now();
        LocalDate currentDateMinus6Months = currentDate.minusMonths(6);
        Instant
            removeDate = currentDateMinus6Months.atStartOfDay(ZoneId.systemDefault()).toInstant();

        JPAQuery<Long> sqlGreetingQuery =
            this.jpaQueryFactory.select(greetingMessage.greetingId.id).from(greetingMessage)
                                .where(greetingMessage.greetingDate.lt(removeDate));

        QueryResults<Long> greetingResults = sqlGreetingQuery.fetchResults();

        Set<GreetingId>greetingIds = new HashSet<>();

        if (greetingResults != null && !greetingResults.isEmpty()) {

            for (Long id : greetingResults.getResults()) {
                greetingIds.add(new GreetingId(id));
            }
        }

        List<GreetingMessage> greetingMessagesList = this.greetingRepository.findAllById(greetingIds);

        for (GreetingMessage greetingMessage : greetingMessagesList) {
            this.greetingRepository.save(greetingMessage.isDeleted(true));
        }

        return new RemoveGreetingCommand.Output(true);
    }

}
