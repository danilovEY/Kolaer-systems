package ru.kolaer.asmc.mvp.view;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;

/**
 * Created by danilovey on 21.02.2017.
 */
public class VSplitListContentImpl implements VSplitListContent {
    private BorderPane mainPane;
    private SplitPane splitPane;

    public VSplitListContentImpl() {
        this.mainPane = new BorderPane();
        this.splitPane = new SplitPane();
        this.init();
    }

    private void init() {
        this.mainPane.getStylesheets().add(this.getClass().getResource("/CSS/Default/Default.css").toString());

        this.splitPane.setOrientation(Orientation.HORIZONTAL);
        this.splitPane.getItems().addAll(null, null);

        this.mainPane.setCenter(this.splitPane);
    }

    @Override
    public void setContent(BorderPane content) {
        this.mainPane.setCenter(content);
    }

    @Override
    public BorderPane getContent() {
        return this.mainPane;
    }

    @Override
    public void setVGroupList(VGroupTree vGroupTree) {
        this.splitPane.getItems().set(0, vGroupTree.getContent());
        SplitPane.setResizableWithParent(vGroupTree.getContent(), Boolean.FALSE);
        this.splitPane.setDividerPosition(0, 0.3f);
    }

    @Override
    public void setVContentLabel(VContentLabel vContentLabel) {
        this.splitPane.getItems().set(1, vContentLabel.getContent());
    }
}
