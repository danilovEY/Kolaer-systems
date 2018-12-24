package ru.kolaer.common.system.network.kolaerweb;

import ru.kolaer.common.system.network.ChatTable;

/**
 * Created by Danilov on 31.07.2016.
 */
public interface ApplicationDataBase {
    GeneralEmployeesTable getGeneralEmployeesTable();
    NotifyMessageTable getNotifyMessageTable();
    CounterTable getCounterTable();
    KolpassTable getKolpassTable();
    ChatTable getChatTable();

    EmployeeOtherOrganizationTable getEmployeeOtherOrganizationTable();
}
