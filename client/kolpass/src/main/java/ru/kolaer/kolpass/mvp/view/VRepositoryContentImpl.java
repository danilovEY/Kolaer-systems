package ru.kolaer.kolpass.mvp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPassword;
import ru.kolaer.api.mvp.model.kolaerweb.kolpass.RepositoryPasswordBase;
import ru.kolaer.api.tools.Tools;

import java.util.function.Function;

/**
 * Created by danilovey on 09.02.2017.
 */
public class VRepositoryContentImpl implements VRepositoryContent {
    private final BorderPane mainPane;
    private final FlowPane contentPane;
    private final MenuItem addRepItem;

    public VRepositoryContentImpl() {
        this.addRepItem = new MenuItem("Добавить репозиторий");

        final ContextMenu contextMenu = new ContextMenu(this.addRepItem);

        this.contentPane = new FlowPane(10, 10);
        this.contentPane.setAlignment(Pos.TOP_CENTER);
        this.contentPane.setPadding(new Insets(10));
        this.contentPane.setStyle("-fx-background-image: url('" + this.getClass().getResource("/background.jpg").toString() + "')");

        final ScrollPane scrollPane = new ScrollPane(this.contentPane);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        this.mainPane = new BorderPane(scrollPane);
        this.mainPane.setOnContextMenuRequested(e -> {
            contextMenu.show(this.mainPane, e.getScreenX(), e.getScreenY());
            e.consume();
        });

        this.mainPane.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> contextMenu.hide());
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
    public void addRepositoryPassword(VRepositoryPassword vRepositoryPassword) {
        Tools.runOnWithOutThreadFX(() ->
                this.contentPane.getChildren().add(vRepositoryPassword.getContent())
        );
    }

    @Override
    public void removeRepositoryPassword(VRepositoryPassword vRepositoryPassword) {
        Tools.runOnWithOutThreadFX(() ->
            this.contentPane.getChildren().remove(vRepositoryPassword.getContent())
        );
    }

    @Override
    public void clear() {
        this.contentPane.getChildren().clear();
    }

    @Override
    public void setOnAddRepository(Function<RepositoryPassword, Void> function) {
        this.addRepItem.setOnAction(e -> {
            final TextInputDialog dialog = new TextInputDialog();
            dialog.setHeaderText("");
            dialog.setContentText("Введите имя репозитория");
            dialog.setTitle("Создание репозитория");
            dialog.showAndWait()
                    .map(this::createRep)
                    .ifPresent(function::apply);
        });
    }

    private RepositoryPassword createRep(String nameRep) {
        RepositoryPassword repositoryPassword = new RepositoryPasswordBase();
        repositoryPassword.setName(nameRep);
        return repositoryPassword;
    }
}
