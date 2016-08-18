package ru.kolaer.client.javafx.system.network.kolaerweb;

import ru.kolaer.api.system.network.kolaerweb.ApplicationDataBase;
import ru.kolaer.api.system.network.kolaerweb.GeneralEmployeesTable;
import ru.kolaer.api.system.network.kolaerweb.NotifyMessageTable;
import ru.kolaer.api.system.network.kolaerweb.PsrTable;

/**
 * Created by Danilov on 31.07.2016.
 */
public class ApplicationDataBaseImpl implements ApplicationDataBase {
    private GeneralEmployeesTable generalEmployeesTable;
    private PsrTable psrTable;
    private NotifyMessageTable notifyMessageTable;

    public ApplicationDataBaseImpl(String path) {
        this.generalEmployeesTable = new GeneralEmployeesTableImpl(path + "/general" + "/employees");
        this.psrTable = new PsrTableImpl(path + "/psr");
        this.notifyMessageTable = new NotifyMessageTableImpl(path + "/notify");
    }

    @Override
    public GeneralEmployeesTable getGeneralEmployeesTable() {
        return this.generalEmployeesTable;
    }

    @Override
    public NotifyMessageTable getNotifyMessageTable() {
        return null;
    }

    @Override
    public PsrTable getPsrTable() {
        return this.psrTable;
    }
}
