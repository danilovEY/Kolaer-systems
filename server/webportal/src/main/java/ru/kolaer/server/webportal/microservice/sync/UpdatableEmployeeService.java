package ru.kolaer.server.webportal.microservice.sync;

import org.springframework.core.Ordered;
import ru.kolaer.server.webportal.common.dto.ResultUpdate;

public interface UpdatableEmployeeService extends Ordered {
    void updateEmployee(ResultUpdate resultUpdate);

    @Override
    default int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
