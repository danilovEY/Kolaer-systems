package ru.kolaer.server.webportal.microservice.ticket.converter;

import ru.kolaer.server.webportal.common.converter.BaseConverter;
import ru.kolaer.server.webportal.microservice.ticket.dto.BankAccountDto;
import ru.kolaer.server.webportal.microservice.ticket.entity.BankAccountEntity;

public interface BankAccountConverter extends BaseConverter<BankAccountDto, BankAccountEntity> {
}
