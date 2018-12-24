package ru.kolaer.common.system.ui;

import javafx.scene.Node;
import ru.kolaer.common.mvp.view.BaseView;

/**
 * Created by danilovey on 25.08.2016.
 */
public interface StaticView extends BaseView<StaticView, Node> {
    default String getTitle() {
        return null;
    }
}
