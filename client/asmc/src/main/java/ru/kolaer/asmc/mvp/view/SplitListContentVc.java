package ru.kolaer.asmc.mvp.view;

import javafx.scene.Node;
import ru.kolaer.common.mvp.view.BaseView;
import ru.kolaer.asmc.mvp.presenter.Access;

/**
 * Created by danilovey on 21.02.2017.
 */
public interface SplitListContentVc extends BaseView<SplitListContentVc, Node>, Access {
    void setView(GroupTreeVc groupTreeVc, ContentLabelVc contentLabelVc);
}
