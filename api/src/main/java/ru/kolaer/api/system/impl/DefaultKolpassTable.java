package ru.kolaer.api.system.impl;

import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordDto;
import ru.kolaer.api.system.network.kolaerweb.KolpassTable;

/**
 * Created by danilovey on 13.02.2017.
 */
public class DefaultKolpassTable implements KolpassTable {
    @Override
    public RepositoryPasswordDto[] getAllRepositoryPasswords() {
        return new RepositoryPasswordDto[0];
    }
}
