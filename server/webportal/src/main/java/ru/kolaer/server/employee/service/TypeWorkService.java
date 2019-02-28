package ru.kolaer.server.employee.service;

import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.kolaerweb.typework.TypeWorkDto;
import ru.kolaer.server.core.service.DefaultService;
import ru.kolaer.server.employee.model.request.FindTypeWorkRequest;

public interface TypeWorkService extends DefaultService<TypeWorkDto> {

    TypeWorkDto updateTypeWork(long typeWorkId, TypeWorkDto request);

    void deleteTypeWork(long typeWorkId);

    PageDto<TypeWorkDto> getAll(FindTypeWorkRequest request);
}
