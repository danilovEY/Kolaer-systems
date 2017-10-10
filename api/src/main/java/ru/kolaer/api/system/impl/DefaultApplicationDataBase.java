package ru.kolaer.api.system.impl;

import ru.kolaer.api.system.network.kolaerweb.*;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultApplicationDataBase implements ApplicationDataBase {
    private final GeneralEmployeesTable generalEmployeesTable = new DefaultGeneralEmployeesTable();
    private final NotifyMessageTable notifyMessageTable = new DefaultNotifyMessageTable();
    private final CounterTable counterTable = new DefaultCounterTable();
    private final KolpassTable kolpassTable = new DefaultKolpassTable();
    private final EmployeeOtherOrganizationTable employeeOtherOrganizationTable = new DefaultEmployeeOtherOrganizationTable();

    @Override
    public GeneralEmployeesTable getGeneralEmployeesTable() {
        return this.generalEmployeesTable;
    }

    @Override
    public NotifyMessageTable getNotifyMessageTable() {
        return this.notifyMessageTable;
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
