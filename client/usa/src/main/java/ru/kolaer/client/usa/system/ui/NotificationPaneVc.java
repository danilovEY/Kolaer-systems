package ru.kolaer.client.usa.system.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.error.ServerExceptionMessage;
import ru.kolaer.api.mvp.view.BaseView;
import ru.kolaer.api.system.ui.NotificationUS;
import ru.kolaer.api.system.ui.NotifyAction;
import ru.kolaer.api.tools.Tools;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by danilovey on 13.02.2018.
 */
@Slf4j
public class NotificationPaneVc implements BaseView<NotificationPaneVc, VBox>, NotificationUS {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy | HH:mm:ss");
    private final int SIMPLE_MESSAGE = 0;
    private final int INFO_MESSAGE = 1;
    private final int WARN_MESSAGE = 2;
    private final int ERROR_MESSAGE = 3;

    private VBox vBoxNotifyMessage;

    @Override
    public void initView(Consumer<NotificationPaneVc> viewVisit) {
        vBoxNotifyMessage = new VBox();
        vBoxNotifyMessage.setSpacing(5);
        vBoxNotifyMessage.setAlignment(Pos.TOP_LEFT);
        vBoxNotifyMessage.setPadding(new Insets(5,5,5,5));

        BackgroundImage notifyBackground= new BackgroundImage(new Image(getClass().getResource("/notify-background.jpg").toString()),
                BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        vBoxNotifyMessage.setBackground(new Background(notifyBackground));

        viewVisit.accept(this);
    }

    @Override
    public VBox getContent() {
        return vBoxNotifyMessage;
    }

    private void sendMessage(int type, String title, String text) {
        this.sendMessage(type, title, text, Collections.emptyList());
    }

    private void sendMessage(VBox typePane, int type, String title, String text, List<NotifyAction> actions) {
        sendMessage(typePane, type, title, text, new Date(), actions);
    }

    private void sendMessage(VBox typePane, int type, String title, String text, Date date, List<NotifyAction> actions) {
        Tools.runOnWithOutThreadFX(() -> {
            VBox content = new VBox();
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

            content.setSpacing(3);

            if(title != null) {
                Label titleLabel = new Label(title);
                titleLabel.setTextAlignment(TextAlignment.CENTER);
                titleLabel.setWrapText(true);
                titleLabel.setMinHeight(Region.USE_PREF_SIZE);
                titleLabel.setFont(Font.font(null, FontWeight.BOLD, 15));

                content.getChildren().add(titleLabel);
            }

            if(text != null) {
                Label textLabel = new Label(text);
                textLabel.setTextAlignment(TextAlignment.CENTER);
                textLabel.setWrapText(true);
                textLabel.setMinHeight(Region.USE_PREF_SIZE);
                textLabel.setFont(Font.font(null, FontWeight.BOLD, 12));

                content.getChildren().add(textLabel);
            }

            String dateToString = dateFormat.format(Optional.ofNullable(date)
                    .orElse(new Date()));

            MenuItem copy = new MenuItem("Копировать");
            MenuItem delete = new MenuItem("Удалить");
            delete.setOnAction(e -> typePane.getChildren().remove(content));
            copy.setOnAction(e -> {
                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.putString(String.format("%s\n%s\n%s", title, text, dateToString));
                Clipboard.getSystemClipboard().setContent(clipboardContent);
            });
            ContextMenu contextMenu = new ContextMenu(copy, delete);

            content.setOnMousePressed(event -> {
                if (event.isSecondaryButtonDown()) {
                    contextMenu.show(content, event.getScreenX(), event.getScreenY());
                }
            });

            Label timeLabel = new Label(dateToString);
            timeLabel.setTextAlignment(TextAlignment.CENTER);
            timeLabel.setWrapText(true);
            timeLabel.setFont(Font.font(null, FontWeight.BOLD, 9));

            content.getChildren().add(timeLabel);

            if(actions != null) {
                for (NotifyAction action : actions) {
                    Button button = new Button(action.getText());
                    button.setOnAction(action.getConsumer()::accept);
                    Tooltip tooltip = new Tooltip();
                    tooltip.setText(action.getText());
                    button.setTooltip(tooltip);
                    content.getChildren().add(button);
                }
            }

            /*final Node border = Borders.wrap(content)
                    .lineBorder()
                    .thickness(5)
                    .innerPadding(0)
                    .radius(5, 5, 5, 5)
                    .color(Color.color(0.114, 0.161, 0.209))
                    .build()
                    .build();*/

            typePane.getChildren().add(content);

            content.toBack();
        });
    }

    private void sendMessage(int type, String title, String text, List<NotifyAction> actions) {
        this.sendMessage(vBoxNotifyMessage, type, title, text, actions);
    }

    @Override
    public void showSimpleNotify(String title, String text) {
        this.sendMessage(SIMPLE_MESSAGE, title, text);
    }

    @Override
    public void showErrorNotify(String title, String text) {
        this.sendMessage(ERROR_MESSAGE, title, text);
    }

    @Override
    public void showWarningNotify(String title, String text) {
        this.sendMessage(WARN_MESSAGE, title, text);
    }

    @Override
    public void showInformationNotify(String title, String text) {
        this.sendMessage(INFO_MESSAGE, title, text);
    }

    @Override
    public void showInformationNotify(String title, String text, Duration duration) {
        this.sendMessage(INFO_MESSAGE, title, text);
    }

    @Override
    public void showSimpleNotify(String title, String text, Duration duration) {
        this.sendMessage(SIMPLE_MESSAGE, title, text);
    }

    @Override
    public void showSimpleNotify(String title, String text, Duration duration, Pos pos, List<NotifyAction> actions) {
        this.sendMessage(SIMPLE_MESSAGE, title, text, actions);
    }

    @Override
    public void showSimpleNotify(String title, String text, Duration duration, List<NotifyAction> actions) {
        this.sendMessage(SIMPLE_MESSAGE, title, text, actions);
    }

    @Override
    public void showErrorNotify(String title, String text, List<NotifyAction> actions) {
        this.sendMessage(ERROR_MESSAGE, title, text, actions);
    }

    @Override
    public void showWarningNotify(String title, String text, List<NotifyAction> actions) {
        this.sendMessage(WARN_MESSAGE, title, text, actions);
    }

    @Override
    public void showInformationNotify(String title, String text, Duration duration, Pos pos, List<NotifyAction> actions) {
        this.sendMessage(INFO_MESSAGE, title, text, actions);    }

    @Override
    public void showInformationNotify(String title, String text, Duration duration, List<NotifyAction> actions) {
        this.sendMessage(INFO_MESSAGE, title, text, actions);
    }

    @Override
    public void showErrorNotify(ServerExceptionMessage exceptionMessage) {
        log.info("Server error: {}", exceptionMessage.toString());

        if(exceptionMessage.getStatus() != 0) {
            String title = exceptionMessage.getCode().getMessage();
            String message = "(" + exceptionMessage.getStatus() + ") " +
                    exceptionMessage.getUrl() +
                    " - " + exceptionMessage.getMessage();

            this.sendMessage(this.vBoxNotifyMessage, ERROR_MESSAGE, title, message,
                    exceptionMessage.getExceptionTimestamp(), Collections.emptyList());
        }
    }

    @Override
    public void showErrorNotify(Exception ex) {
        log.error("Ошибка в потоке: {}", Thread.currentThread().getName(), ex);
        Tools.runOnWithOutThreadFX(() -> showErrorNotify("Ошибка!", ex.getLocalizedMessage()));
    }

}
