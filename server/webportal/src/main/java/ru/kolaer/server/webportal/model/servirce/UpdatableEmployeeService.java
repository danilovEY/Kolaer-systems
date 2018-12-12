package ru.kolaer.server.webportal.model.servirce;

import org.springframework.core.Ordered;
import ru.kolaer.server.webportal.model.dto.ResultUpdate;

public interface UpdatableEmployeeService extends Ordered {
    void updateEmployee(ResultUpdate resultUpdate);

    @Override
    default int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
