package ru.kolaer.api.system.network.kolaerweb;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;

/**
 * Created by Danilov on 31.07.2016.
 */
public interface PsrStatusTable {
    PsrStatus[] getAllPsrStatus();
}
