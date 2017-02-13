package ru.kolaer.api.system.impl;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.api.system.network.kolaerweb.PsrStatusTable;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultPsrStatusTable implements PsrStatusTable {

    @Override
    public PsrStatus[] getAllPsrStatus() {
        return new PsrStatus[0];
    }
}
