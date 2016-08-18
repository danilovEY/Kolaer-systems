package ru.kolaer.client.javafx.system.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
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

/**
 * Created by danilovey on 18.08.2016.
 */
public class NotificationPane implements NotificationUS, VComponentUI {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationPane.class);
    private BorderPane mainPane;
    private VBox vBox;

    public NotificationPane() {
        this.vBox = new VBox();
        this.vBox.setSpacing(5);
        this.vBox.setAlignment(Pos.TOP_LEFT);
        this.vBox.setPadding(new Insets(5,5,5,5));
        final ScrollPane scrollPane = new ScrollPane(this.vBox);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        this.mainPane = new BorderPane(scrollPane);
        this.mainPane.setMinWidth(300);
        this.mainPane.setPrefWidth(300);
        this.mainPane.setMaxWidth(400);
        scrollPane.setMinWidth(300);
        scrollPane.setPrefWidth(300);
        scrollPane.setMaxWidth(400);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        BackgroundImage myBI= new BackgroundImage(new Image(this.getClass().getResource("/notify-background.jpg").toString()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        this.vBox.setBackground(new Background(myBI));
    }

    @Override
    public void showSimpleNotifi(String title, String text) {
        this.sendMessage(title, text);
    }

    @Override
    public void showErrorNotifi(String title, String text) {
        this.sendMessage(title, text);
    }

    @Override
    public void showWarningNotifi(String title, String text) {
        this.sendMessage(title, text);
    }

    @Override
    public void showInformationNotifi(String title, String text) {
        this.sendMessage(title, text);
    }

    @Override
    public void showInformationNotifi(String title, String text, Duration duration) {
        this.sendMessage(title, text);
    }

    @Override
    public void showSimpleNotifi(String title, String text, Duration duration) {
        this.sendMessage(title, text);
    }

    @Override
    public void showSimpleNotifi(String title, String text, Duration duration, Pos pos, NotifiAction... actions) {
        this.sendMessage(title, text, actions);
    }

    @Override
    public void showSimpleNotifi(String title, String text, Duration duration, NotifiAction... actions) {
        this.sendMessage(title, text, actions);    }

    @Override
    public void showErrorNotifi(String title, String text, NotifiAction... actions) {
        this.sendMessage(title, text, actions);;
    }

    @Override
    public void showWarningNotifi(String title, String text, NotifiAction... actions) {
        this.sendMessage(title, text, actions);
    }

    @Override
    public void showInformationNotifi(String title, String text, Duration duration, Pos pos, NotifiAction... actions) {
        this.sendMessage(title, text, actions);    }

    @Override
    public void showInformationNotifi(String title, String text, Duration duration, NotifiAction... actions) {
        this.sendMessage(title, text, actions);
    }

    private void sendMessage(String title, String text) {
        this.sendMessage(title, text, null);
    }

    private void sendMessage(String title, String text, NotifiAction... actions) {
        Tools.runOnThreadFX(() -> {
            final VBox content = new VBox();
            content.setAlignment(Pos.CENTER);
            content.setBackground(Background.EMPTY);
            content.setStyle("-fx-background-color: rgba(58, 188, 255, 0.6); -fx-effect: dropshadow(gaussian , #868686, 4,0,0,1 ); -fx-padding: 3;");
            content.setSpacing(5);
            final Label titleLabel = new Label(title);
            titleLabel.setFont(Font.font(null, FontWeight.BOLD, 15));

            final Label textLabel = new Label(text);
            textLabel.setFont(Font.font(null, FontWeight.BOLD, 15));
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
            this.vBox.getChildren().add(border);
        });
    }

    @Override
    public void setContent(Parent content) {

    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }
}
