package ru.kolaer.server.service.ticket.converter;

import ru.kolaer.server.service.ticket.dto.BankAccountDto;
import ru.kolaer.server.service.ticket.entity.BankAccountEntity;
import ru.kolaer.server.webportal.common.converter.BaseConverter;

public interface BankAccountConverter extends BaseConverter<BankAccountDto, BankAccountEntity> {
}
