package ru.kolaer.asmc.mvp.view;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

import java.util.function.Consumer;

/**
 * Created by danilovey on 21.02.2017.
 */
public class SplitListContentVcImpl implements SplitListContentVc {
    private BorderPane mainPane;
    private SplitPane splitPane;

    private boolean isAccess = false;
    private GroupTreeVc groupList;
    private ContentLabelVc contentLabel;

    public SplitListContentVcImpl(GroupTreeVc groupList, ContentLabelVc contentLabel) {
        this.groupList = groupList;
        this.contentLabel = contentLabel;
    }

    @Override
    public void setAccess(boolean access) {
        this.contentLabel.setAccess(access);
        this.groupList.setAccess(access);
        this.isAccess = access;
    }

    @Override
    public boolean isAccess() {
        return this.isAccess;
    }

    @Override
    public BorderPane getContent() {
        return this.mainPane;
    }

    @Override
    public void setView(GroupTreeVc groupTreeVc) {
        this.splitPane.getItems().set(0, groupTreeVc.getContent());
        SplitPane.setResizableWithParent(groupTreeVc.getContent(), Boolean.FALSE);
        this.splitPane.setDividerPosition(0, 0.3f);
    }

    @Override
    public void setView(ContentLabelVc contentLabelVc) {
        this.splitPane.getItems().set(1, contentLabelVc.getContent());
    }

    @Override
    public void initView(Consumer<SplitListContentVc> viewVisit) {
        mainPane = new BorderPane();
        splitPane = new SplitPane();

        mainPane.getStylesheets().add(this.getClass().getResource("/CSS/Default/Default.css").toString());

        splitPane.setOrientation(Orientation.HORIZONTAL);
        splitPane.getItems().addAll(null, null);

        mainPane.setCenter(this.splitPane);

        groupList.setOnSelectItem(contentLabel::setSelectedGroup);
    }
}
