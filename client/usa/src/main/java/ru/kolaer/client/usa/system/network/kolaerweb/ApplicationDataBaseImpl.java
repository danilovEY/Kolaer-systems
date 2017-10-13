package ru.kolaer.client.usa.system.network.kolaerweb;

import org.springframework.web.client.RestTemplate;
import ru.kolaer.api.system.network.kolaerweb.*;

/**
 * Created by Danilov on 31.07.2016.
 */
public class ApplicationDataBaseImpl implements ApplicationDataBase {
    private final RestTemplate globalRestTemplate;
    private final GeneralEmployeesTable generalEmployeesTable;
    private final PsrTable psrTable;
    private final NotifyMessageTable notifyMessageTable;
    private final CounterTable counterTable;
    private final EmployeeOtherOrganizationTable employeeOtherOrganizationTable;
    private final KolpassTable kolpassTable;

    public ApplicationDataBaseImpl(RestTemplate globalRestTemplate, String path) {
        this.globalRestTemplate = globalRestTemplate;

        this.generalEmployeesTable = new GeneralEmployeesTableImpl(this.globalRestTemplate,
                path + "/employees");

        this.psrTable = new PsrTableImpl(this.globalRestTemplate, path + "/psr");

        this.notifyMessageTable = new NotifyMessageTableImpl(this.globalRestTemplate,
                path + "/non-security/notify");

        this.counterTable = new CounterTableImpl(this.globalRestTemplate,
                path + "/non-security/counters");

        this.employeeOtherOrganizationTable = new EmployeeOtherOrganizationTableImpl(this.globalRestTemplate,
                path + "/organizations/employees");

        this.kolpassTable = new KolpassTableImpl(this.globalRestTemplate,
                path + "/kolpass");
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
