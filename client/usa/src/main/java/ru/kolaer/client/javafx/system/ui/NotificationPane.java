package ru.kolaer.client.javafx.system.ui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import org.controlsfx.tools.Borders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kolaer.api.mvp.view.VComponentUI;
import ru.kolaer.api.system.ui.NotifiAction;
import ru.kolaer.api.system.ui.NotificationUS;
import ru.kolaer.api.tools.Tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by danilovey on 18.08.2016.
 */
public class NotificationPane implements NotificationUS, VComponentUI {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationPane.class);
    private final int SIMPLE_MESSAGE = 0;
    private final int INFO_MESSAGE = 1;
    private final int WARN_MESSAGE = 2;
    private final int ERROR_MESSAGE = 3;
    private BorderPane mainPane;
    private VBox vBoxUserNotify;
    private VBox vBoxAdminNotify;

    public NotificationPane() {
        this.vBoxUserNotify = new VBox();
        this.vBoxUserNotify.setSpacing(5);
        this.vBoxUserNotify.setAlignment(Pos.TOP_LEFT);
        this.vBoxUserNotify.setPadding(new Insets(5,5,5,5));

        final ScrollPane scrollPaneUserNotify = new ScrollPane(this.vBoxUserNotify);
        scrollPaneUserNotify.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneUserNotify.setMinWidth(300);
        scrollPaneUserNotify.setPrefWidth(300);
        scrollPaneUserNotify.setMaxWidth(400);
        scrollPaneUserNotify.setFitToHeight(true);
        scrollPaneUserNotify.setFitToWidth(true);


        this.vBoxAdminNotify = new VBox();
        this.vBoxAdminNotify.setSpacing(5);
        this.vBoxAdminNotify.setAlignment(Pos.TOP_LEFT);
        this.vBoxAdminNotify.setPadding(new Insets(5,5,5,5));

        final ScrollPane scrollPaneAdminNotify = new ScrollPane(this.vBoxAdminNotify);
        scrollPaneAdminNotify.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneAdminNotify.setMinWidth(300);
        scrollPaneAdminNotify.setMinHeight(100);
        scrollPaneAdminNotify.setPrefWidth(300);
        scrollPaneAdminNotify.setPrefHeight(100);
        scrollPaneAdminNotify.setMaxWidth(400);
        //scrollPaneAdminNotify.setMaxHeight(400);
        scrollPaneAdminNotify.setFitToHeight(true);
        scrollPaneAdminNotify.setFitToWidth(true);

        final SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(scrollPaneUserNotify, scrollPaneAdminNotify);
        splitPane.setDividerPositions(1);
        splitPane.setOrientation(Orientation.VERTICAL);
        this.mainPane = new BorderPane(splitPane);
        this.mainPane.setMinWidth(300);
        this.mainPane.setPrefWidth(300);
        this.mainPane.setMaxWidth(400);


        BackgroundImage myBI= new BackgroundImage(new Image(this.getClass().getResource("/notify-background.jpg").toString()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        this.vBoxUserNotify.setBackground(new Background(myBI));
        this.vBoxAdminNotify.setBackground(new Background(myBI));
    }

    @Override
    public void showSimpleNotifi(String title, String text) {
        this.sendMessage(SIMPLE_MESSAGE, title, text);
    }

    @Override
    public void showErrorNotifi(String title, String text) {
        this.sendMessage(ERROR_MESSAGE, title, text);
    }

    @Override
    public void showWarningNotifi(String title, String text) {
        this.sendMessage(WARN_MESSAGE, title, text);
    }

    @Override
    public void showInformationNotifi(String title, String text) {
        this.sendMessage(INFO_MESSAGE, title, text);
    }

    @Override
    public void showInformationNotifi(String title, String text, Duration duration) {
        this.sendMessage(INFO_MESSAGE, title, text);
    }

    @Override
    public void showSimpleNotifi(String title, String text, Duration duration) {
        this.sendMessage(SIMPLE_MESSAGE, title, text);
    }

    @Override
    public void showSimpleNotifi(String title, String text, Duration duration, Pos pos, NotifiAction... actions) {
        this.sendMessage(SIMPLE_MESSAGE, title, text, actions);
    }

    @Override
    public void showSimpleNotifi(String title, String text, Duration duration, NotifiAction... actions) {
        this.sendMessage(SIMPLE_MESSAGE, title, text, actions);
    }

    @Override
    public void showErrorNotifi(String title, String text, NotifiAction... actions) {
        this.sendMessage(ERROR_MESSAGE, title, text, actions);
    }

    @Override
    public void showWarningNotifi(String title, String text, NotifiAction... actions) {
        this.sendMessage(WARN_MESSAGE, title, text, actions);
    }

    @Override
    public void showInformationNotifi(String title, String text, Duration duration, Pos pos, NotifiAction... actions) {
        this.sendMessage(INFO_MESSAGE, title, text, actions);    }

    @Override
    public void showInformationNotifi(String title, String text, Duration duration, NotifiAction... actions) {
        this.sendMessage(INFO_MESSAGE, title, text, actions);
    }

    @Override
    public void showInformationNotifiAdmin(String title, String text, NotifiAction... actions) {
        this.sendMessage(this.vBoxAdminNotify, 1, title, text, actions);
    }

    @Override
    public void showWarningNotifiAdmin(String title, String text, NotifiAction... actions) {
        this.sendMessage(this.vBoxAdminNotify, 2, title, text, actions);
    }

    private void sendMessage(int type, String title, String text) {
        this.sendMessage(type, title, text, new NotifiAction[0]);
    }

    private void sendMessage(VBox typePane, int type, String title, String text, NotifiAction... actions) {
        Tools.runOnThreadFX(() -> {
            final VBox content = new VBox();
            content.setAlignment(Pos.CENTER);
            content.setBackground(Background.EMPTY);
            switch (type) {
                case SIMPLE_MESSAGE: {
                    content.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6); -fx-effect: dropshadow(gaussian , #868686, 4,0,0,1 ); -fx-padding: 3;");
                    break;
                }

                case INFO_MESSAGE: {
                    content.setStyle("-fx-background-color: rgba(58, 188, 255, 0.6); -fx-effect: dropshadow(gaussian , #868686, 4,0,0,1 ); -fx-padding: 3;");
                    break;
                }

                case WARN_MESSAGE: {
                    content.setStyle("-fx-background-color: rgba(255, 255, 0, 0.6); -fx-effect: dropshadow(gaussian , #866432, 4,0,0,1 ); -fx-padding: 3;");
                    break;
                }

                case ERROR_MESSAGE: {
                    content.setStyle("-fx-background-color: rgba(255, 0, 0, 0.6); -fx-effect: dropshadow(gaussian , #861a3d, 4,0,0,1 ); -fx-padding: 3;");
                    break;
                }

                default: {
                    content.setStyle("-fx-background-color: rgba(255, 255, 255, 0.6); -fx-effect: dropshadow(gaussian , #868686, 4,0,0,1 ); -fx-padding: 3;");
                }
            }

            content.setSpacing(5);

            final Label timeLabel = new Label(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss | dd.MM.yyyy")));
            timeLabel.setFont(Font.font(null, FontWeight.BOLD, 10));

            final Label titleLabel = new Label(title);
            titleLabel.setFont(Font.font(null, FontWeight.BOLD, 15));

            final Label textLabel = new Label(text);
            textLabel.setFont(Font.font(null, FontWeight.BOLD, 15));

            content.getChildren().add(timeLabel);
            content.getChildren().add(titleLabel);
            content.getChildren().add(textLabel);

            if(actions != null) {
                for (NotifiAction action : actions) {
                    final Button button = new Button(action.getText());
                    button.setOnAction(action.getConsumer()::accept);
                    final Tooltip tooltip = new Tooltip();
                    tooltip.setText(action.getText());
                    button.setTooltip(tooltip);
                    content.getChildren().add(button);
                }
            }

            final Node border = Borders.wrap(content)
                    .lineBorder()
                    .thickness(5)
                    .innerPadding(0)
                    .radius(5, 5, 5, 5)
                    .color(Color.color(0.114, 0.161, 0.209))
                    .build()
                    .build();
            typePane.getChildren().add(border);
        });
    }

    private void sendMessage(int type, String title, String text, NotifiAction... actions) {
        this.sendMessage(this.vBoxUserNotify, type, title, text, actions);
    }

    @Override
    public void setContent(Parent content) {

    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }
}
