package ru.kolaer.kolpass.mvp.presenter;

import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistory;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.kolpass.mvp.view.VPasswordHistory;
import ru.kolaer.kolpass.mvp.view.VPasswordHistoryImpl;

import java.text.SimpleDateFormat;
import java.util.Optional;

/**
 * Created by danilovey on 09.02.2017.
 */
@Slf4j
public class PPasswordHistoryImpl implements PPasswordHistory {
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private VPasswordHistory vPasswordHistory;
    private RepositoryPasswordHistory passwordHistoryDto;

    public PPasswordHistoryImpl() {
        this.vPasswordHistory = new VPasswordHistoryImpl();
    }

    public PPasswordHistoryImpl(RepositoryPasswordHistory passwordHistoryDto) {
        this();
        this.setModel(passwordHistoryDto);
    }

    @Override
    public void updateView() {
        this.vPasswordHistory.setDate(null);
        this.vPasswordHistory.setPassword(null);
        this.vPasswordHistory.setLogin(null);

        Optional.ofNullable(this.passwordHistoryDto).ifPresent(pass -> {
            Optional.ofNullable(pass.getPasswordWriteDate()).ifPresent(date ->
                    this.vPasswordHistory.setDate(this.sdf.format(date))
            );
            this.vPasswordHistory.setPassword(pass.getPassword());
            this.vPasswordHistory.setLogin(pass.getLogin());
        });
    }

    @Override
    public RepositoryPasswordHistory getModel() {
        return this.passwordHistoryDto;
    }

    @Override
    public void setModel(RepositoryPasswordHistory model) {
        this.passwordHistoryDto = model;
        this.updateView();
    }

    @Override
    public VPasswordHistory getView() {
        return this.vPasswordHistory;
    }

    @Override
    public void setView(VPasswordHistory view) {
        this.vPasswordHistory = view;
        this.updateView();
    }

    @Override
    public void setEditable(boolean edit) {
        this.vPasswordHistory.setEditable(edit);
    }

    @Override
    public boolean isEditable() {
        return this.vPasswordHistory.isEditable();
    }
}
