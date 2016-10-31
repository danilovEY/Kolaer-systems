package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.system.network.restful.EmployeeOtherOrganizationTable;

/**
 * Created by Danilov on 31.07.2016.
 */
public interface ApplicationDataBase {
    GeneralEmployeesTable getGeneralEmployeesTable();
    NotifyMessageTable getNotifyMessageTable();
    PsrTable getPsrTable();
    CounterTable getCounterTable();
    /**Получить объект для работы с таблицой BirthdayAll.*/
    EmployeeOtherOrganizationTable getEmployeeOtherOrganizationTable();
}
