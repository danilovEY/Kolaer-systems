package ru.kolaer.api.system.impl;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.exceptions.ServerException;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegisterDto;
import ru.kolaer.api.system.network.kolaerweb.PsrTable;

import java.util.Optional;

/**
 * Created by danilovey on 13.02.2017.
 */
@Slf4j
public class DefaultPsrTable implements PsrTable {

    @Override
    public PsrRegisterDto[] getAllPsrRegister() throws ServerException {
        return new PsrRegisterDto[0];
    }

    @Override
    public PsrRegisterDto persistPsrRegister(PsrRegisterDto psrRegister) throws ServerException {
        log.info("Добавлен ПСР реестр: {}", Optional.ofNullable(psrRegister).orElse(new PsrRegisterDto()).getName());
        return null;
    }

    @Override
    public void deletePsrRegister(PsrRegisterDto psrRegister) throws ServerException {
        log.info("Удален ПСР реестр: {}", Optional.ofNullable(psrRegister).orElse(new PsrRegisterDto()).getName());
    }

    @Override
    public void updatePsrRegister(PsrRegisterDto psrRegister) throws ServerException {
        log.info("Обновлен ПСР реестр: {}", Optional.ofNullable(psrRegister).orElse(new PsrRegisterDto()).getName());
    }
}
