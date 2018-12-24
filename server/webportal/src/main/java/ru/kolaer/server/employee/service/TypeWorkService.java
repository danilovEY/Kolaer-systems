package ru.kolaer.server.employee.service;

import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.kolaerweb.typework.TypeWorkDto;
import ru.kolaer.server.core.service.DefaultService;
import ru.kolaer.server.webportal.model.dto.typework.FindTypeWorkRequest;

public interface TypeWorkService extends DefaultService<TypeWorkDto> {

    TypeWorkDto updateTypeWork(long typeWorkId, TypeWorkDto request);

    void deleteTypeWork(long typeWorkId);

    Page<TypeWorkDto> getAll(FindTypeWorkRequest request);
}
