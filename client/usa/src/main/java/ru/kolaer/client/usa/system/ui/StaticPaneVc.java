package ru.kolaer.client.usa.system.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import ru.kolaer.client.core.mvp.view.BaseView;
import ru.kolaer.client.core.system.ui.StaticUS;
import ru.kolaer.client.core.system.ui.StaticView;
import ru.kolaer.client.core.tools.Tools;

import java.util.function.Consumer;

/**
 * Created by danilovey on 13.02.2018.
 */
public class StaticPaneVc implements BaseView<StaticPaneVc, VBox>, StaticUS {
    private VBox vBoxStatic;

    @Override
    public void addStaticView(StaticView staticView) {
        Tools.runOnWithOutThreadFX(() -> {
            //BorderPane content = new BorderPane();
            //content.setBackground(Background.EMPTY);
            //content.setStyle("-fx-background-color: rgba(255,251,253,0.8); -fx-effect: dropshadow(gaussian , #858086, 4,0,0,1 ); -fx-padding: 3;");
            //content.setCenter(staticView.getContent());

            /*Node border = Borders.wrap(content)
                    .lineBorder()
                    .thickness(5)
                    .innerPadding(0)
                    .radius(5, 5, 5, 5)
                    .color(Color.color(0.114, 0.161, 0.209))
                    .build()
                    .build();*/

            //if(StringUtils.isEmpty(staticView.getContent().getStyle())) {
            //    staticView.getContent().setStyle("-fx-background-color: rgba(255,251,253,0.8); -fx-effect: dropshadow(gaussian , #858086, 4,0,0,1 ); -fx-padding: 3;");
            //}

            Node viewContent = staticView.getContent();

            if(!vBoxStatic.getChildren().contains(viewContent)) {
                vBoxStatic.getChildren().add(viewContent);

                MenuItem upView = new MenuItem("Поднять");
                MenuItem downView = new MenuItem("Опустить");
                MenuItem deleteView = new MenuItem("Удалить");
                upView.setOnAction(e -> viewContent.toBack());
                downView.setOnAction(e -> viewContent.toFront());
                deleteView.setOnAction(e ->  vBoxStatic.getChildren().remove(viewContent));

                ContextMenu contextMenuContent = new ContextMenu(upView,
                        downView,
                        new SeparatorMenuItem(),
                        deleteView);

                viewContent.setOnMousePressed(event -> {
                    if (event.isSecondaryButtonDown()) {
                        contextMenuContent.show(viewContent, event.getScreenX(), event.getScreenY());
                    }
                });
            }

            viewContent.toBack();
        });
    }

    @Override
    public void removeStaticView(StaticView staticView) {
        Tools.runOnWithOutThreadFX(() -> vBoxStatic.getChildren().remove(staticView.getContent()));
    }

    @Override
    public void initView(Consumer<StaticPaneVc> viewVisit) {
        vBoxStatic = new VBox();
        vBoxStatic.setSpacing(5);
        vBoxStatic.setAlignment(Pos.TOP_LEFT);
        vBoxStatic.setPadding(new Insets(5,5,5,5));

        BackgroundImage staticBackground= new BackgroundImage(new Image(getClass().getResource("/static-background.jpg").toString()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        this.vBoxStatic.setBackground(new Background(staticBackground));

        viewVisit.accept(this);
    }

    @Override
    public VBox getContent() {
        return vBoxStatic;
    }
}
