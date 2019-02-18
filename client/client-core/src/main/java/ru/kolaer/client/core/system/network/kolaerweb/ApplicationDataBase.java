package ru.kolaer.client.core.system.network.kolaerweb;

import ru.kolaer.client.core.system.network.ChatTable;

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
