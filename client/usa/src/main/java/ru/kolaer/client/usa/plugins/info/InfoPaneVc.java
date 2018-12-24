package ru.kolaer.client.usa.plugins.info;

import javafx.scene.Parent;
import ru.kolaer.common.mvp.view.BaseView;
import ru.kolaer.common.system.ui.StaticUS;

/**
 * Created by danilovey on 13.02.2018.
 */
public interface InfoPaneVc extends BaseView<InfoPaneVc, Parent>, StaticUS, Thread.UncaughtExceptionHandler {
}
