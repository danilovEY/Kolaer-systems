package ru.kolaer.client.javafx.system.network.kolaerweb;

import ru.kolaer.api.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.api.system.network.kolaerweb.GeneralEmployeesTable;
import ru.kolaer.api.system.network.kolaerweb.PsrTable;

/**
 * Created by Danilov on 31.07.2016.
 */
public class ApplicationDataBaseImpl implements ApplicationDataBase {
    private GeneralEmployeesTable generalEmployeesTable;
    private PsrTable psrTable;

    public ApplicationDataBaseImpl() {
        this.generalEmployeesTable = new GeneralEmployeesTableImpl();
        this.psrTable = new PsrTableImpl();
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
