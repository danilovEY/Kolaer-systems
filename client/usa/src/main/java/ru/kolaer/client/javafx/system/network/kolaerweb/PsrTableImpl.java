package ru.kolaer.client.javafx.system.network.kolaerweb;

import com.sun.jersey.api.client.WebResource;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrRegister;
import ru.kolaer.api.system.network.kolaerweb.PsrTable;

import java.util.List;

/**
 * Created by Danilov on 31.07.2016.
 */
public class PsrTableImpl implements PsrTable {

    public PsrTableImpl(WebResource psr) {

    }

    @Override
    public List<PsrRegister> getAllPsrRegister() {
        return null;
    }
}
