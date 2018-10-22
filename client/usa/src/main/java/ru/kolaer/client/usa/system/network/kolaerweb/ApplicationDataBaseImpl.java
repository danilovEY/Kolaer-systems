package ru.kolaer.client.usa.system.network.kolaerweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;
import ru.kolaer.common.system.network.ChatTable;
import ru.kolaer.common.system.network.kolaerweb.*;
import ru.kolaer.client.usa.system.network.ChatTableImpl;

/**
 * Created by Danilov on 31.07.2016.
 */
public class ApplicationDataBaseImpl implements ApplicationDataBase {
    private final GeneralEmployeesTable generalEmployeesTable;
    private final NotifyMessageTable notifyMessageTable;
    private final CounterTable counterTable;
    private final EmployeeOtherOrganizationTable employeeOtherOrganizationTable;
    private final KolpassTable kolpassTable;
    private final ChatTable chatTable;

    public ApplicationDataBaseImpl(ObjectMapper objectMapper, RestTemplate globalRestTemplate, String path) {
        this.generalEmployeesTable = new GeneralEmployeesTableImpl(objectMapper, globalRestTemplate,
                path + "/employees");

        this.notifyMessageTable = new NotifyMessageTableImpl(objectMapper, globalRestTemplate,
                path + "/non-security/notify");

        this.counterTable = new CounterTableImpl(objectMapper, globalRestTemplate,
                path + "/non-security/counters");

        this.employeeOtherOrganizationTable = new EmployeeOtherOrganizationTableImpl(objectMapper, globalRestTemplate,
                path + "/organizations/employees");

        this.kolpassTable = new KolpassTableImpl(objectMapper, globalRestTemplate,
                path + "/kolpass");
        this.chatTable = new ChatTableImpl(path + "/chat", globalRestTemplate, objectMapper);
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

    public ChatTable getChatTable() {
        return chatTable;
    }
}
