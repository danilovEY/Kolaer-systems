package ru.kolaer.api.system.network.kolaerweb;

/**
 * Created by Danilov on 31.07.2016.
 */
public interface ApplicationDataBase {
    GeneralEmployeesTable getGeneralEmployeesTable();
    NotifyMessageTable getNotifyMessageTable();
    PsrTable getPsrTable();
    CounterTable getCounterTable();
    KolpassTable getKolpassTable();

    EmployeeOtherOrganizationTable getEmployeeOtherOrganizationTable();
}
