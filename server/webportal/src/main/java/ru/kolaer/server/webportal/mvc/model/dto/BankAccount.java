package ru.kolaer.server.webportal.mvc.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;

/**
 * Created by danilovey on 13.12.2016.
 */
@Data
@AllArgsConstructor
@ApiModel("(Банк) Данные банка")
public class BankAccount {
    @ApiModelProperty("Сотрудник")
    private EmployeeEntity employeeEntity;
    @ApiModelProperty("Счет")
    private String check;
}
