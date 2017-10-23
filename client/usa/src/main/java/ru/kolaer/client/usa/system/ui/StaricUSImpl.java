package ru.kolaer.client.usa.system.ui;

import ru.kolaer.api.system.ui.StaticUS;
import ru.kolaer.api.system.ui.StaticView;
import ru.kolaer.client.usa.system.UniformSystemEditorKitSingleton;

/**
 * Created by danilovey on 25.08.2016.
 */
public class StaricUSImpl implements StaticUS {

    @Override
    public void addStaticView(StaticView staticView) {
        UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification().showParentNotify(staticView.getContent());
    }

    @Override
    public void removeStaticView(StaticView staticView) {
        UniformSystemEditorKitSingleton.getInstance().getUISystemUS().getNotification().removeParentNotify(staticView.getContent());
    }
}
