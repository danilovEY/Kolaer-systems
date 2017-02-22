package ru.kolaer.asmc.mvp.view;

import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.layout.BorderPane;
import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.mvp.model.MLabel;

import java.util.function.Function;

/**
 * Created by danilovey on 20.02.2017.
 */
public class VLabelImpl implements VLabel {
    private BorderPane mainPane;

    public VLabelImpl() {
        this.mainPane = new BorderPane();
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
    public void updateView(MLabel label) {
        this.mainPane.setCenter(new Label(label.getName()));
    }

    @Override
    public void setOnEdit(Function<MLabel, Void> function) {

    }

    @Override
    public void setOnDelete(Function<MLabel, Void> function) {

    }

    @Override
    public void setAccess(boolean access) {

    }
}
