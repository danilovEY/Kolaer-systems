package ru.kolaer.client.javafx.system.network.kolaerweb;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.api.system.network.kolaerweb.*;

/**
 * Created by Danilov on 31.07.2016.
 */
public class ApplicationDataBaseImpl implements ApplicationDataBase {
    private final GeneralEmployeesTable generalEmployeesTable;
    private final PsrTable psrTable;
    private final NotifyMessageTable notifyMessageTable;

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
        return this.notifyMessageTable;
    }

    @Override
    public PsrTable getPsrTable() {
        return this.psrTable;
    }
}
