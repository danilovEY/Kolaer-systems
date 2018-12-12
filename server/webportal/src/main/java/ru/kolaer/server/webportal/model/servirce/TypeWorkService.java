package ru.kolaer.server.webportal.model.servirce;

import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.api.mvp.model.kolaerweb.typework.TypeWorkDto;
import ru.kolaer.server.webportal.model.dto.typework.FindTypeWorkRequest;

public interface TypeWorkService extends DefaultService<TypeWorkDto> {

    TypeWorkDto updateTypeWork(long typeWorkId, TypeWorkDto request);

    void deleteTypeWork(long typeWorkId);

    Page<TypeWorkDto> getAll(FindTypeWorkRequest request);
}
