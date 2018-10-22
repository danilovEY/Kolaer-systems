package ru.kolaer.kolpass.mvp.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import ru.kolaer.common.mvp.model.kolaerweb.kolpass.PasswordRepositoryDto;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by danilovey on 09.02.2017.
 */
public class VRepositoryContentImpl implements VRepositoryContent {
    private BorderPane mainPane;
    private FlowPane contentPane;
    private MenuItem addRepItem;
    private ScrollPane scrollPane;
    private ContextMenu contextMenu;

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
        this.contentPane.getChildren().add(vRepositoryPassword.getContent());
    }

    @Override
    public void removeRepositoryPassword(VRepositoryPassword vRepositoryPassword) {
        this.contentPane.getChildren().remove(vRepositoryPassword.getContent());
    }

    @Override
    public void clear() {
        this.contentPane.getChildren().clear();
    }

    @Override
    public void setContextMenu(boolean enable) {
        this.scrollPane.setContextMenu(enable ? this.contextMenu : null);
    }

    @Override
    public void setOnAddRepository(Function<PasswordRepositoryDto, Void> function) {
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

    private PasswordRepositoryDto createRep(String nameRep) {
        PasswordRepositoryDto repositoryPassword = new PasswordRepositoryDto();
        repositoryPassword.setName(nameRep);
        return repositoryPassword;
    }

    @Override
    public void initView(Consumer<VRepositoryContent> viewVisit) {
        this.addRepItem = new MenuItem("Добавить репозиторий");

        this.contextMenu = new ContextMenu(this.addRepItem);

        this.contentPane = new FlowPane(10, 10);
        this.contentPane.setAlignment(Pos.TOP_CENTER);
        this.contentPane.setPadding(new Insets(10));
        this.contentPane.setStyle("-fx-background-image: url('" + this.getClass().getResource("/background.jpg").toString() + "')");

        this.scrollPane = new ScrollPane(this.contentPane);
        this.scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        this.scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.scrollPane.setFitToHeight(true);
        this.scrollPane.setFitToWidth(true);

        this.mainPane = new BorderPane(this.scrollPane);

        viewVisit.accept(this);
    }
}
