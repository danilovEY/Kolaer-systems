package ru.kolaer.server.core.service;

import org.springframework.core.Ordered;
import ru.kolaer.server.core.model.dto.ResultUpdate;

public interface UpdatableEmployeeService extends Ordered {
    void updateEmployee(ResultUpdate resultUpdate);

    @Override
    default int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
