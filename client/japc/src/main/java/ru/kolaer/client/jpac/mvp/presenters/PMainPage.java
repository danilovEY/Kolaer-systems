package ru.kolaer.client.jpac.mvp.presenters;

import ru.kolaer.client.jpac.mvp.views.VMainPage;

/**
 * Created by danilovey on 06.09.2016.
 */
public class PMainPage {
    private VMainPage view;

    public PMainPage() {
        this.view = new VMainPage();
    }

    public VMainPage getView() {
        return view;
    }

    public void setView(VMainPage vMainPage) {
        this.view = vMainPage;
    }
}
