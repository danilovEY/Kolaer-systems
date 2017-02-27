package ru.kolaer.asmc.mvp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import ru.kolaer.asmc.mvp.model.MGroup;
import ru.kolaer.asmc.mvp.model.MLabel;
import ru.kolaer.asmc.mvp.presenter.CAddingLabelDialog;

import java.util.Optional;
import java.util.function.Function;

/**
 * Created by danilovey on 21.02.2017.
 */
public class VContentLabelImpl implements VContentLabel {
    private final ContextMenu contextMenu;
    private final BorderPane mainPane;
    private final FlowPane contentPane;
    private final MenuItem addLabel;
    private final ScrollPane scrollPane;

    public VContentLabelImpl() {
        this.mainPane = new BorderPane();
        this.contentPane = new FlowPane();
        this.scrollPane = new ScrollPane(this.contentPane);
        this.addLabel = new MenuItem("Добавить ярлык");
        this.contextMenu = new ContextMenu(this.addLabel);
        this.init();
    }

    private void init() {
        this.contentPane.setStyle("-fx-background-image: url(/background-repiat.jpg);");
        this.contentPane.setAlignment(Pos.TOP_CENTER);
        this.contentPane.setPadding(new Insets(5,5,5,5));
        this.contentPane.setVgap(5);
        this.contentPane.setHgap(5);

        this.scrollPane.setContextMenu(this.contextMenu);
        this.scrollPane.setFitToHeight(true);
        this.scrollPane.setFitToWidth(true);

        this.mainPane.setCenter(scrollPane);
    }

    @Override
    public void addVLabel(VLabel label) {
        this.contentPane.getChildren().add(label.getContent());
    }

    @Override
    public void removeVLabel(VLabel label) {
        this.contentPane.getChildren().remove(label.getContent());
    }

    @Override
    public void clear() {
        this.contentPane.getChildren().clear();
    }

    @Override
    public void setOnAddLabel(Function<MLabel, Void> function) {
        this.addLabel.setOnAction(e ->
            new CAddingLabelDialog().showAndWait()
                    .ifPresent(function::apply)
        );
    }

    @Override
    public void setContent(BorderPane content) {
        this.mainPane.setCenter(content);
    }

    @Override
    public BorderPane getContent() {
        return this.mainPane;
    }

}
