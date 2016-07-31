package ru.kolaer.client.javafx.system.network.kolaerweb;

import com.sun.jersey.api.client.WebResource;
import ru.kolaer.api.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.api.system.network.kolaerweb.GeneralEmployeesTable;
import ru.kolaer.api.system.network.kolaerweb.PsrTable;

/**
 * Created by Danilov on 31.07.2016.
 */
public class ApplicationDataBaseImpl implements ApplicationDataBase {
    private GeneralEmployeesTable generalEmployeesTable;
    private PsrTable psrTable;

    public ApplicationDataBaseImpl(WebResource path) {
        this.generalEmployeesTable = new GeneralEmployeesTableImpl(path.path("general").path("employees"));
        this.psrTable = new PsrTableImpl(path.path("psr"));
    }

    @Override
    public GeneralEmployeesTable getGeneralEmployeesTable() {
        return this.generalEmployeesTable;
    }

    @Override
    public PsrTable getPsrTable() {
        return this.psrTable;
    }
}
