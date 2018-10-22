package ru.kolaer.server.webportal.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.kolaer.common.mvp.model.kolaerweb.AccountSimpleDto;
import ru.kolaer.common.mvp.model.kolaerweb.EmployeeDto;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by danilovey on 26.01.2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResultUpdate implements Serializable {
    private List<EmployeeDto> deleteEmployee = Collections.emptyList();
    private List<EmployeeDto> addEmployee = Collections.emptyList();

    private List<AccountSimpleDto> blockAccounts = Collections.emptyList();
    private List<AccountSimpleDto> addAccounts = Collections.emptyList();
}
