package ru.kolaer.client.usa.plugins.info;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import ru.kolaer.client.core.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.client.core.system.ui.StaticView;
import ru.kolaer.client.core.tools.Tools;
import ru.kolaer.client.usa.mvp.viewmodel.impl.ImageViewPane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by danilovey on 13.02.2018.
 */
@Slf4j
public class InfoPaneVcImpl implements InfoPaneVc {
    private transient static final Object synch = new Object();

    private BorderPane mainPane;
    private FlowPane staticViewPane;

    private List<StaticView> chacheStaticView = new ArrayList<>(5);

    @Override
    public void initView(Consumer<InfoPaneVc> viewVisit) {
        mainPane = new BorderPane();

        staticViewPane = new FlowPane(Orientation.HORIZONTAL);
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

        if(!chacheStaticView.isEmpty()) {
            chacheStaticView.forEach(this::addStaticView);
            chacheStaticView.clear();
        }

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
        if(!isViewInit()) {
            chacheStaticView.add(staticView);
        } else {
            Tools.runOnWithOutThreadFX(() -> {
                Node newContent = Borders.wrap(staticView.getContent())
                        .lineBorder()
                        .title("  " + staticView.getTitle())
                        .thickness(5)
                        .innerPadding(20)
                        .radius(10)
                        .color(Color.color(0.114, 0.161, 0.209))
                        .buildAll();

//            newContent.setStyle("-fx-max-height: " + newContent.getBoundsInParent().getHeight());

                staticView.setContent(newContent);

                staticViewPane.getChildren().add(staticView.getContent());

//            synchronized (synch) {
                ObservableList<Node> nodes = FXCollections.observableArrayList(staticViewPane.getChildren());

                nodes.sort((o1, o2) ->
                        Double.compare(o1.getBoundsInParent().getHeight(), o2.getBoundsInParent().getHeight()) * -1
                );

                staticViewPane.getChildren().setAll(nodes);
//            }
            });
        }
    }

    @Override
    public void removeStaticView(StaticView staticView) {
        if(!isViewInit()) {
            chacheStaticView.remove(staticView);
        } else {
            Tools.runOnWithOutThreadFX(() -> {
                staticViewPane.getChildren().remove(staticView.getContent());
            });
        }
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        UniformSystemEditorKitSingleton.getInstance()
                .getUISystemUS()
                .getNotification()
                .showErrorNotify("Ошибка", e.getLocalizedMessage());
    }
}
