package ru.kolaer.client.psr.mvp.presenter.impl;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.system.UniformSystemEditorKit;
import ru.kolaer.client.psr.mvp.presenter.PMainPane;
import ru.kolaer.client.psr.mvp.presenter.PPsrRegisterTable;
import ru.kolaer.client.psr.mvp.view.VMainPane;
import ru.kolaer.client.psr.mvp.view.impl.VMainPaneImpl;

/**
 * Created by danilovey on 01.08.2016.
 */
public class PMainPaneImpl implements PMainPane {
    private final VMainPane view;
    private final UniformSystemEditorKit editorKit;
    private final PPsrRegisterTable pPsrRegisterTable;

    public PMainPaneImpl(UniformSystemEditorKit editorKit) {
        this.editorKit = editorKit;
        this.pPsrRegisterTable = new PPsrRegisterTableImpl(editorKit);
        this.view = new VMainPaneImpl();

    }

    @Override
    public VMainPane getView() {
        return this.view;
    }

    @Override
    public void updatePluginPage() {
        this.view.initializationView();
        pPsrRegisterTable.updateTableData();
        this.view.setContent(pPsrRegisterTable.getView().getContent());
    }

    @Override
    public void login(GeneralAccountsEntity account) {

    }

    @Override
    public void logout(GeneralAccountsEntity account) {

    }
}
