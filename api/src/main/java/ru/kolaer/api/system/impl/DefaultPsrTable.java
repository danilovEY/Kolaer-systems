package ru.kolaer.api.system.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegisterBase;
import ru.kolaer.api.system.network.kolaerweb.PsrStatusTable;
import ru.kolaer.api.system.network.kolaerweb.PsrTable;

import java.util.Optional;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class DefaultPsrTable implements PsrTable {
    private PsrStatusTable psrStatusTable = new DefaultPsrStatusTable();

    @Override
    public PsrStatusTable getPsrStatusTable() {
        return this.psrStatusTable;
    }

    @Override
    public PsrRegister[] getAllPsrRegister() throws ServerException {
        return new PsrRegister[0];
    }

    @Override
    public PsrRegister persistPsrRegister(PsrRegister psrRegister) throws ServerException {
        log.info("Добавлен ПСР реестр: {}", Optional.ofNullable(psrRegister).orElse(new PsrRegisterBase()).getName());
        return null;
    }

    @Override
    public void deletePsrRegister(PsrRegister psrRegister) throws ServerException {
        log.info("Удален ПСР реестр: {}", Optional.ofNullable(psrRegister).orElse(new PsrRegisterBase()).getName());
    }

    @Override
    public void updatePsrRegister(PsrRegister psrRegister) throws ServerException {
        log.info("Обновлен ПСР реестр: {}", Optional.ofNullable(psrRegister).orElse(new PsrRegisterBase()).getName());
    }
}
