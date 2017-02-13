package ru.kolaer.kolpass.mvp.presenter;

import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordDto;
import ru.kolaer.api.system.network.kolaerweb.KolpassTable;
import ru.kolaer.kolpass.mvp.view.VRepositoryPane;
import ru.kolaer.kolpass.mvp.view.VRepositoryPaneImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danilovey on 09.02.2017.
 */
public class PRepositoryPaneImpl implements PRepositoryPane {
    private final List<PRepositoryPassword> repositoryPasswordList = new ArrayList<>();
    private VRepositoryPane view;
    private KolpassTable kolpassTable;

    public PRepositoryPaneImpl() {
        this.view = new VRepositoryPaneImpl();
    }

    @Override
    public VRepositoryPane getView() {
        return this.view;
    }

    @Override
    public void setView(VRepositoryPane view) {
        this.view = view;
    }

    @Override
    public void updateView() {
        this.view.clear();
        this.repositoryPasswordList.clear();

        final RepositoryPasswordDto[] allRepositoryPasswords = this.kolpassTable.getAllRepositoryPasswords();
        for (RepositoryPasswordDto repositoryPassword : allRepositoryPasswords) {
            this.addRepositoryPassword(new PRepositoryPasswordImpl(repositoryPassword));
        }
    }

    @Override
    public void addRepositoryPassword(PRepositoryPassword pRepositoryPassword) {
        this.repositoryPasswordList.add(pRepositoryPassword);
        this.view.addRepositoryPassword(pRepositoryPassword.getView());
    }

    @Override
    public void removeRepositoryPassword(PRepositoryPassword pRepositoryPassword) {
        this.repositoryPasswordList.remove(pRepositoryPassword);
        this.view.removeRepositoryPassword(pRepositoryPassword.getView());
    }

    @Override
    public KolpassTable getModel() {
        return this.kolpassTable;
    }

    @Override
    public void setModel(KolpassTable model) {
        this.kolpassTable = model;
        this.updateView();
    }
}
