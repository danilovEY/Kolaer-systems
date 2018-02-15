package ru.kolaer.client.usa.plugins.info;

import javafx.scene.Parent;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.api.system.ui.StaticUS;

/**
 * Created by danilovey on 13.02.2018.
 */
public interface InfoPaneVc extends BaseView<InfoPaneVc, Parent>, StaticUS, Thread.UncaughtExceptionHandler {
}
