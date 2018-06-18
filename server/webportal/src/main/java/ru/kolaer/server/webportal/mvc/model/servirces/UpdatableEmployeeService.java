package ru.kolaer.server.webportal.mvc.model.servirces;

import org.springframework.core.Ordered;
import ru.kolaer.server.webportal.mvc.model.dto.ResultUpdate;

public interface UpdatableEmployeeService extends Ordered {
    void updateEmployee(ResultUpdate resultUpdate);

    @Override
    default int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
