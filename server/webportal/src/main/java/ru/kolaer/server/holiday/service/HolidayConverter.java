package ru.kolaer.server.holiday.service;

import ru.kolaer.server.core.converter.BaseConverter;
import ru.kolaer.server.holiday.model.entity.HolidayEntity;
import ru.kolaer.server.webportal.model.dto.holiday.HolidayDto;

public interface HolidayConverter extends BaseConverter<HolidayDto, HolidayEntity> {
}
