package ru.kolaer.kolpass.mvp.presenter;

import ru.kolaer.kolpass.mvp.view.VRepositoryPane;
import ru.kolaer.kolpass.mvp.view.VRepositoryPaneImpl;

/**
 * Created by danilovey on 09.02.2017.
 */
public class PRepositoryPaneImpl implements PRepositoryPane {
    private VRepositoryPane view;

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

    }
}
