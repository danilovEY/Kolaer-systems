package ru.kolaer.server.employee.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.server.employee.model.dto.EmploymentHistoryDto;
import ru.kolaer.server.employee.model.entity.EmploymentHistoryEntity;

import javax.validation.constraints.NotNull;

@Service
public class EmploymentHistoryMapper {

    @NotNull
    public EmploymentHistoryDto mapToEmploymentHistoryDto(@NotNull EmploymentHistoryEntity entity) {
        EmploymentHistoryDto employmentHistory = new EmploymentHistoryDto();
        employmentHistory.setId(entity.getId());
        employmentHistory.setEmployeeId(entity.getEmployeeId());
        employmentHistory.setEndWorkDate(entity.getEndWorkDate());
        employmentHistory.setStartWorkDate(entity.getStartWorkDate());
        employmentHistory.setOrganization(entity.getOrganization());
        employmentHistory.setPost(entity.getPost());

        return employmentHistory;
    }

}
