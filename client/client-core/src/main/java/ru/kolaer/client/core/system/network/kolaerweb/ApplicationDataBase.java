package ru.kolaer.client.core.system.network.kolaerweb;

import ru.kolaer.client.core.system.network.ChatTable;
import ru.kolaer.client.core.system.network.HolidaysTable;

/**
 * Created by Danilov on 31.07.2016.
 */
public interface ApplicationDataBase {
    GeneralEmployeesTable getGeneralEmployeesTable();
    NotifyMessageTable getNotifyMessageTable();
    CounterTable getCounterTable();
    KolpassTable getKolpassTable();
    ChatTable getChatTable();
    HolidaysTable getHolidaysTable();


    EmployeeOtherOrganizationTable getEmployeeOtherOrganizationTable();
}
