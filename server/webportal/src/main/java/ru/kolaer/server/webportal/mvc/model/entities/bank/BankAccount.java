package ru.kolaer.server.webportal.mvc.model.entities.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;

/**
 * Created by danilovey on 13.12.2016.
 */
@Data
@AllArgsConstructor
public class BankAccount {
    private EmployeeEntity employeeEntity;
    private String check;
}
