package ru.kolaer.kolpass.mvp.presenter;

import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordDto;
import ru.kolaer.kolpass.mvp.view.VRepositoryPassword;
import ru.kolaer.kolpass.mvp.view.VRepositoryPasswordImpl;

/**
 * Created by danilovey on 09.02.2017.
 */
public class PRepositoryPasswordImpl implements PRepositoryPassword {
    private VRepositoryPassword vRepositoryPassword;
    private RepositoryPasswordDto passwordDto;
    private PPasswordHistory lastPassword;
    private PPasswordHistory firstPassword;
    private PPasswordHistory prevPassword;

    public PRepositoryPasswordImpl() {
        this.vRepositoryPassword = new VRepositoryPasswordImpl();
        this.lastPassword = new PPasswordHistoryImpl();
        this.firstPassword = new PPasswordHistoryImpl();
        this.prevPassword = new PPasswordHistoryImpl();
    }

    public PRepositoryPasswordImpl(RepositoryPasswordDto passwordDto) {
        this();
        this.passwordDto = passwordDto;
        this.updateView();
    }

    @Override
    public void updateView() {
        this.vRepositoryPassword.setName(this.passwordDto.getName());

        this.lastPassword.setModel(this.passwordDto.getLastPassword());
        this.firstPassword.setModel(this.passwordDto.getFirstPassword());
        this.prevPassword.setModel(this.passwordDto.getPrevPassword());

        this.vRepositoryPassword.setFirstPassword(this.firstPassword.getView());
        this.vRepositoryPassword.setFirstPassword(this.lastPassword.getView());
        this.vRepositoryPassword.setPrevPassword(this.prevPassword.getView());
    }

    @Override
    public RepositoryPasswordDto getModel() {
        return this.passwordDto;
    }

    @Override
    public void setModel(RepositoryPasswordDto model) {
        this.passwordDto = model;
    }

    @Override
    public VRepositoryPassword getView() {
        return this.vRepositoryPassword;
    }

    @Override
    public void setView(VRepositoryPassword view) {
        this.vRepositoryPassword = view;
    }
}
