package ru.kolaer.kolpass.mvp.presenter;

import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordHistoryBase;
import ru.kolaer.kolpass.mvp.view.VPasswordHistory;
import ru.kolaer.kolpass.mvp.view.VPasswordHistoryImpl;

import java.text.SimpleDateFormat;

/**
 * Created by danilovey on 09.02.2017.
 */
public class PPasswordHistoryImpl implements PPasswordHistory {
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    private VPasswordHistory vPasswordHistory;
    private RepositoryPasswordHistoryBase passwordHistoryDto;

    public PPasswordHistoryImpl() {
        this.vPasswordHistory = new VPasswordHistoryImpl();
    }

    public PPasswordHistoryImpl(RepositoryPasswordHistoryBase passwordHistoryDto) {
        this();
        this.setModel(passwordHistoryDto);
    }

    @Override
    public void updateView() {
        this.vPasswordHistory.setDate(this.sdf.format(this.passwordHistoryDto.getPasswordWriteDate()));
        this.vPasswordHistory.setPassword(this.passwordHistoryDto.getPassword());
        this.vPasswordHistory.setLogin(this.passwordHistoryDto.getLogin());
    }

    @Override
    public RepositoryPasswordHistoryBase getModel() {
        return this.passwordHistoryDto;
    }

    @Override
    public void setModel(RepositoryPasswordHistoryBase model) {
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
