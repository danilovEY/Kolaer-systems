package ru.kolaer.server.employee.converter;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.employee.model.dto.StaffMovementDto;
import ru.kolaer.server.employee.model.entity.StaffMovementEntity;

import javax.validation.constraints.NotNull;

@Service
@Validated
public class StaffMovementMapper {

    @NotNull
    public StaffMovementDto convertToDto(@NotNull StaffMovementEntity entity) {
        StaffMovementDto staffMovementDto = new StaffMovementDto();
        staffMovementDto.setId(entity.getId());
        staffMovementDto.setCardSlam(entity.getCardSlam());
        staffMovementDto.setCategoryUnit(entity.getCategoryUnit());
        staffMovementDto.setClassWorkingConditions(entity.getClassWorkingConditions());
        staffMovementDto.setSubClassWorkingConditions(entity.getSubClassWorkingConditions());
        staffMovementDto.setDepartment(entity.getDepartment());
        staffMovementDto.setEmployeeId(entity.getEmployeeId());
        staffMovementDto.setEndWorkDate(entity.getEndWorkDate());
        staffMovementDto.setStartWorkDate(entity.getStartWorkDate());
        staffMovementDto.setName(entity.getName());
        staffMovementDto.setOrderNumber(entity.getOrderNumber());
        staffMovementDto.setOrderDate(entity.getOrderDate());
        staffMovementDto.setPost(entity.getPost());
        staffMovementDto.setSalary(entity.getSalary());
        staffMovementDto.setSurchargeHarmfulness(entity.getSurchargeHarmfulness());

        return staffMovementDto;
    }

}
