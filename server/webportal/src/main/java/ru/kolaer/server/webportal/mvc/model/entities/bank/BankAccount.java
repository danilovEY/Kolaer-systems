package ru.kolaer.server.webportal.mvc.model.entities.bank;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;

/**
 * Created by danilovey on 13.12.2016.
 */
@Data
@AllArgsConstructor
public class BankAccount {
    private GeneralEmployeesEntity generalEmployeesEntity;
    private String check;
}
