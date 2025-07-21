package com.thitsaworks.operation_portal.usecase.operation_portal.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.operation_portal.component.common.type.UserRoleType;
import com.thitsaworks.operation_portal.component.misc.exception.DomainException;
import com.thitsaworks.operation_portal.core.audit.command.CreateExceptionAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateInputAuditCommand;
import com.thitsaworks.operation_portal.core.audit.command.CreateOutputAuditCommand;
import com.thitsaworks.operation_portal.core.iam.cache.PrincipalCache;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantNDCCommand;
import com.thitsaworks.operation_portal.core.participant.command.CreateParticipantNDCHistoryCommand;
import com.thitsaworks.operation_portal.core.participant.command.DeleteParticipantNDCCommand;
import com.thitsaworks.operation_portal.core.participant.model.ParticipantNDC;
import com.thitsaworks.operation_portal.core.participant.query.ParticipantNDCQuery;
import com.thitsaworks.operation_portal.usecase.OperationPortalAuditableUseCase;
import com.thitsaworks.operation_portal.usecase.operation_portal.CreateParticipantNDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class CreateParticipantNDCHandler
    extends OperationPortalAuditableUseCase<CreateParticipantNDC.Input, CreateParticipantNDC.Output>
        implements CreateParticipantNDC {

    private static final Logger LOG = LoggerFactory.getLogger(CreateNewContactHandler.class);

    private static final Set<UserRoleType> PERMITTED_ROLES = Set.of(UserRoleType.OPERATION,
                                                                    UserRoleType.ADMIN);

    private final CreateParticipantNDCCommand createParticipantNDCCommand;

    private final CreateParticipantNDCHistoryCommand createParticipantNDCHistoryCommand;

    private final DeleteParticipantNDCCommand deleteParticipantNDCCommand;

    private final ParticipantNDCQuery participantNDCQuery;

    public CreateParticipantNDCHandler(CreateInputAuditCommand createInputAuditCommand,
                                       CreateOutputAuditCommand createOutputAuditCommand,
                                       CreateExceptionAuditCommand createExceptionAuditCommand,
                                       ObjectMapper objectMapper,
                                       PrincipalCache principalCache,
                                       CreateParticipantNDCCommand createParticipantNDCCommand,
                                       CreateParticipantNDCHistoryCommand createParticipantNDCHistoryCommand,
                                       DeleteParticipantNDCCommand deleteParticipantNDCCommand,
                                       ParticipantNDCQuery participantNDCQuery) {

        super(createInputAuditCommand,
              createOutputAuditCommand,
              createExceptionAuditCommand,
              PERMITTED_ROLES,
              objectMapper,
              principalCache);

        this.createParticipantNDCCommand = createParticipantNDCCommand;
        this.createParticipantNDCHistoryCommand = createParticipantNDCHistoryCommand;
        this.deleteParticipantNDCCommand = deleteParticipantNDCCommand;
        this.participantNDCQuery = participantNDCQuery;
    }

    @Override
    protected Output onExecute(Input input) throws DomainException {

        Optional<ParticipantNDC> optionalParticipantNDC = this.currencyRateQuery.findCurrencyRate(input.getPairCode());

        if (currencyRateData.isPresent()) {

            CurrencyRateData existingRate = currencyRateData.get();

            //check rate existingRate currentRate

            if (existingRate.getRate().equals(input.getRate())) {

                throw new CurrencyRateAlreadyExistException(
                        "Currency Rate [" + input.getRate() + "] is already exist.");
            }

            IAddCurrencyRateHistoryCommand.Output currencyRateHistory =
                    this.addCurrencyRateHistoryCommand.execute(new IAddCurrencyRateHistoryCommand.Input(
                            existingRate.getCurrencyRateId(), existingRate.getPairCode(),
                            existingRate.getRate(), existingRate.getRateTime(),
                            existingRate.getExpireAt(), existingRate.getCreatedBy(),
                            existingRate.getCreatedDate(), existingRate.getUpdatedDate(),
                            new UserId(input.getCreateBy().getId())));

            IDeleteCurrencyRateCommand.Output deleteCurrencyRateCommand =
                    this.deleteCurrencyRate.execute(new IDeleteCurrencyRateCommand.Input(existingRate.getPairCode()));

            IAddCurrencyRateCommand.Output currencyRateCommand = this.addCurrencyRateCommand.execute(
                    new IAddCurrencyRateCommand.Input(input.getPairCode(), input.getRate(),
                                                      input.getRateTime(), input.getCreateBy(), input.getExpireAt()));

            if (currencyRateCommand.getRateTime().isAfter(currencyRateCommand.getExpireAt())) {

                throw new ExpireAtShouldBeGreaterThanRateTimeException(
                        "Expire At should be greater than rate time.");
            }

            return new Output(currencyRateCommand.getCurrencyRateId(),
                              currencyRateCommand.getPairCode(),
                              currencyRateCommand.getRate(),
                              currencyRateCommand.getRateTime(),
                              currencyRateCommand.getUserId(),
                              currencyRateCommand.getExpireAt());

        } else {

            IAddCurrencyRateCommand.Output currencyRateCommandWithNewPairCode = this.addCurrencyRateCommand
                    .execute(new IAddCurrencyRateCommand.Input(input.getPairCode(),
                                                               input.getRate(),
                                                               input.getRateTime(),
                                                               input.getCreateBy(),
                                                               input.getExpireAt()));

            if (currencyRateCommandWithNewPairCode.getRateTime()
                                                  .isAfter(currencyRateCommandWithNewPairCode.getExpireAt())) {

                throw new ExpireAtShouldBeGreaterThanRateTimeException(
                        "Expire At should be greater than rate time.");
            }

            return new Output(currencyRateCommandWithNewPairCode.getCurrencyRateId(),
                              currencyRateCommandWithNewPairCode.getPairCode(),
                              currencyRateCommandWithNewPairCode.getRate(),
                              currencyRateCommandWithNewPairCode.getRateTime(),
                              currencyRateCommandWithNewPairCode.getUserId(),
                              currencyRateCommandWithNewPairCode.getExpireAt());

        }

        return new Output(null);
    }

}
