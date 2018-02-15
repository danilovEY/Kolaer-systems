package ru.kolaer.client.usa.plugins.info;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.extern.slf4j.Slf4j;
import org.controlsfx.tools.Borders;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.system.ui.StaticView;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.usa.mvp.viewmodel.impl.ImageViewPane;

import java.util.function.Consumer;

/**
 * Created by danilovey on 13.02.2018.
 */
@Slf4j
public class InfoPaneVcImpl implements InfoPaneVc {
    private BorderPane mainPane;
    private FlowPane staticViewPane;

    @Override
    public void initView(Consumer<InfoPaneVc> viewVisit) {
        mainPane = new BorderPane();

        staticViewPane = new FlowPane(Orientation.VERTICAL);
        staticViewPane.setAlignment(Pos.TOP_CENTER);
        staticViewPane.setHgap(20);
        staticViewPane.setVgap(20);
        staticViewPane.setPadding(new Insets(20));

        BackgroundImage staticBackground = new BackgroundImage(new Image(getClass().getResource("/static-background.jpg").toString()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        staticViewPane.setBackground(new Background(staticBackground));

        ScrollPane scrollPaneStatic = new ScrollPane(staticViewPane);
        scrollPaneStatic.setMinWidth(300);
        scrollPaneStatic.setPrefWidth(300);
        scrollPaneStatic.setFitToHeight(true);
        scrollPaneStatic.setFitToWidth(true);

        mainPane.setCenter(scrollPaneStatic);
        mainPane.setTop(initBanner());

        viewVisit.accept(this);
    }

    private Node initBanner() {
        BorderPane imagePane = new BorderPane();
        imagePane.setStyle("-fx-background-color: #FFFFFF"); //,linear-gradient(#f8f8f8, #e7e7e7);
        imagePane.setMaxHeight(300);
        imagePane.setMaxWidth(Double.MAX_VALUE);

        ImageView left = new ImageView(new Image(getClass().getResource("/LR.png").toString(), true));
        left.setPreserveRatio(false);

        ImageView right = new ImageView(new Image(getClass().getResource("/LR.png").toString(), true));
        right.setPreserveRatio(false);

        ImageViewPane center = new ImageViewPane(new ImageView(new Image(getClass().getResource("/Centr.png").toString(), true)));

        imagePane.setRight(right);
        imagePane.setLeft(left);
        imagePane.setCenter(center);

        return imagePane;
    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }

    @Override
    public void addStaticView(StaticView staticView) {
        Tools.runOnWithOutThreadFX(() -> {
            Node newContent = Borders.wrap(staticView.getContent())
                    .lineBorder()
                    .title("  " + staticView.getTitle())
                    .thickness(5)
                    .innerPadding(20)
                    .radius(10)
                    .color(Color.color(0.114, 0.161, 0.209))
                    .buildAll();

            staticView.setContent(newContent);

            staticViewPane.getChildren().add(staticView.getContent());
        });
    }

    @Override
    public void removeStaticView(StaticView staticView) {
        Tools.runOnWithOutThreadFX(() -> {
            staticViewPane.getChildren().remove(staticView.getContent());
        });
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        UniformSystemEditorKitSingleton.getInstance()
                .getUISystemUS()
                .getNotification()
                .showErrorNotify("Ошибка", e.getLocalizedMessage());
    }
}
