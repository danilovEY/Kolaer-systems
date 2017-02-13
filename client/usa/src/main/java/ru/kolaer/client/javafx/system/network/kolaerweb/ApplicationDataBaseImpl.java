package ru.kolaer.client.javafx.system.network.kolaerweb;

import ru.kolaer.api.system.network.kolaerweb.*;
import ru.kolaer.api.system.network.kolaerweb.EmployeeOtherOrganizationTable;

/**
 * Created by Danilov on 31.07.2016.
 */
public class ApplicationDataBaseImpl implements ApplicationDataBase {
    private final GeneralEmployeesTable generalEmployeesTable;
    private final PsrTable psrTable;
    private final NotifyMessageTable notifyMessageTable;
    private final CounterTable counterTable;
    private final EmployeeOtherOrganizationTable employeeOtherOrganizationTable;
    private final KolpassTable kolpassTable;

    public ApplicationDataBaseImpl(String path) {
        this.generalEmployeesTable = new GeneralEmployeesTableImpl(path + "/employees");
        this.psrTable = new PsrTableImpl(path + "/psr");
        this.notifyMessageTable = new NotifyMessageTableImpl(path + "/non-security/notify");
        this.counterTable = new CounterTableImpl(path + "/non-security/counters");
        this.employeeOtherOrganizationTable = new EmployeeOtherOrganizationTableImpl(path + "/organizations/employees");
        this.kolpassTable = new KolpassTableImpl(path + "/kolpass");
    }

    @Override
    public GeneralEmployeesTable getGeneralEmployeesTable() {
        return this.generalEmployeesTable;
    }

    @Override
    public NotifyMessageTable getNotifyMessageTable() {
        return this.notifyMessageTable;
    }

    @Override
    public PsrTable getPsrTable() {
        return this.psrTable;
    }

    @Override
    public CounterTable getCounterTable() {
        return this.counterTable;
    }

    @Override
    public KolpassTable getKolpassTable() {
        return this.kolpassTable;
    }

    @Override
    public EmployeeOtherOrganizationTable getEmployeeOtherOrganizationTable() {
        return this.employeeOtherOrganizationTable;
    }
}
