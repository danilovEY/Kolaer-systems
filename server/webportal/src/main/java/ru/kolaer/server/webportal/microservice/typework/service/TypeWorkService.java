package ru.kolaer.server.webportal.microservice.typework.service;

import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.common.mvp.model.kolaerweb.typework.TypeWorkDto;
import ru.kolaer.server.webportal.common.servirces.DefaultService;
import ru.kolaer.server.webportal.microservice.typework.dto.FindTypeWorkRequest;

public interface TypeWorkService extends DefaultService<TypeWorkDto> {

    TypeWorkDto updateTypeWork(long typeWorkId, TypeWorkDto request);

    void deleteTypeWork(long typeWorkId);

    Page<TypeWorkDto> getAll(FindTypeWorkRequest request);
}
