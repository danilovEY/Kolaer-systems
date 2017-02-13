package ru.kolaer.kolpass.mvp.presenter;

import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordBase;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistoryBase;
import ru.kolaer.kolpass.mvp.view.VRepositoryPassword;
import ru.kolaer.kolpass.mvp.view.VRepositoryPasswordImpl;

import java.util.Objects;
import java.util.Optional;

/**
 * Created by danilovey on 09.02.2017.
 */
public class PRepositoryPasswordImpl implements PRepositoryPassword {
    private VRepositoryPassword vRepositoryPassword;
    private RepositoryPasswordBase passwordDto;
    private PPasswordHistory lastPassword;
    private PPasswordHistory firstPassword;
    private PPasswordHistory prevPassword;

    public PRepositoryPasswordImpl() {
        this.vRepositoryPassword = new VRepositoryPasswordImpl();
    }

    public PRepositoryPasswordImpl(RepositoryPasswordBase passwordDto) {
        this();
        this.passwordDto = passwordDto;
        this.updateView();
    }

    @Override
    public void updateView() {
        this.vRepositoryPassword.setName(this.passwordDto.getName());

        final String url = this.passwordDto.getUrlImage() == null
                ? this.getClass().getResource("/icon-security1.png").toString()
                : this.passwordDto.getUrlImage();

        this.vRepositoryPassword.setImageUrl(url);

        Optional.ofNullable(this.passwordDto.getLastPassword())
                .filter(Objects::nonNull)
                .map(dto -> this.lastPassword = this.setIfExist(this.lastPassword, dto, true))
                .ifPresent(pass -> this.vRepositoryPassword.setLastPassword(pass.getView()));

        Optional.ofNullable(this.passwordDto.getPrevPassword())
                .filter(Objects::nonNull)
                .map(dto -> this.prevPassword = this.setIfExist(this.prevPassword, dto, false))
                .ifPresent(pass -> this.vRepositoryPassword.setPrevPassword(pass.getView()));

        Optional.ofNullable(this.passwordDto.getFirstPassword())
                .filter(Objects::nonNull)
                .map(dto -> this.firstPassword = this.setIfExist(this.firstPassword, dto, false))
                .ifPresent(pass -> this.vRepositoryPassword.setFirstPassword(pass.getView()));

    }

    @Override
    public RepositoryPasswordBase getModel() {
        return this.passwordDto;
    }

    @Override
    public void setModel(RepositoryPasswordBase model) {
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

    private PPasswordHistory setIfExist(PPasswordHistory pPass, RepositoryPasswordHistoryBase dto, boolean edit) {
        PPasswordHistory pPasswordHistory = Optional.ofNullable(pPass).orElse(new PPasswordHistoryImpl());
        pPasswordHistory.setEditable(edit);
        pPasswordHistory.setModel(dto);
        return pPasswordHistory;
    }
}
