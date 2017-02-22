package ru.kolaer.asmc.mvp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

/**
 * Created by danilovey on 21.02.2017.
 */
public class VContentLabelImpl implements VContentLabel {
    private BorderPane mainPane;
    private FlowPane contentPane;

    public VContentLabelImpl() {
        this.mainPane = new BorderPane();
        this.contentPane = new FlowPane();
        this.init();
    }

    private void init() {
        this.contentPane.setStyle("-fx-background-image: url(/background-repiat.jpg);");
        this.contentPane.setAlignment(Pos.TOP_CENTER);
        this.contentPane.setPadding(new Insets(5,5,5,5));
        this.contentPane.setVgap(5);
        this.contentPane.setHgap(5);

        final ScrollPane scrollPane = new ScrollPane(this.contentPane);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

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
    public void setContent(BorderPane content) {
        this.mainPane.setCenter(content);
    }

    @Override
    public BorderPane getContent() {
        return this.mainPane;
    }
}
