package ru.kolaer.server.ticket.service;

import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.ticket.model.dto.BankAccountDto;
import ru.kolaer.server.ticket.model.entity.BankAccountEntity;

public interface BankAccountConverter extends BaseConverter<BankAccountDto, BankAccountEntity> {
}
