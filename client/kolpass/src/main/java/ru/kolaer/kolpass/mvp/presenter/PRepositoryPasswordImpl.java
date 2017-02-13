package ru.kolaer.kolpass.mvp.presenter;

import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistory;
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
    private RepositoryPassword passwordDto;
    private PPasswordHistory lastPassword;
    private PPasswordHistory firstPassword;
    private PPasswordHistory prevPassword;

    public PRepositoryPasswordImpl() {
        this.vRepositoryPassword = new VRepositoryPasswordImpl();
    }

    public PRepositoryPasswordImpl(RepositoryPassword passwordDto) {
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

        this.lastPassword = this.setIfExist(this.lastPassword, this.passwordDto.getLastPassword(), true);
        this.vRepositoryPassword.setLastPassword(this.lastPassword.getView());
        /*Optional.ofNullable()
                .map(dto -> )
                .ifPresent(pass -> );*/

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
    public RepositoryPassword getModel() {
        return this.passwordDto;
    }

    @Override
    public void setModel(RepositoryPassword model) {
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

    private PPasswordHistory setIfExist(PPasswordHistory pPass, RepositoryPasswordHistory dto, boolean edit) {
        PPasswordHistory pPasswordHistory = Optional.ofNullable(pPass).orElse(new PPasswordHistoryImpl());
        pPasswordHistory.setEditable(edit);
        pPasswordHistory.setModel(Optional.ofNullable(dto).orElse(new RepositoryPasswordHistoryBase()));
        return pPasswordHistory;
    }
}
