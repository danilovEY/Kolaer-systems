package ru.kolaer.client.message.service;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import ru.kolaer.client.core.system.ui.NotificationType;
import ru.kolaer.client.core.system.ui.NotificationView;
import ru.kolaer.client.core.system.ui.NotifyAction;
import ru.kolaer.client.core.tools.Tools;
import ru.kolaer.common.dto.kolaerweb.NotifyMessageDto;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by danilovey on 14.02.2018.
 */
public class NotificationMessageVcImpl implements NotificationView {
    private final NotifyMessageDto notifyMessageDto;

    private Node mainPane;

    public NotificationMessageVcImpl(NotifyMessageDto notifyMessageDto) {
        this.notifyMessageDto = notifyMessageDto;
    }

    @Override
    public void initView(Consumer<NotificationView> viewVisit) {
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        content.setBackground(Background.EMPTY);
        content.setStyle("-fx-background-color: rgba(58, 188, 255, 0.6); -fx-effect: dropshadow(gaussian , #868686, 4,0,0,1 ); -fx-padding: 3;");

        content.setSpacing(3);

        if(notifyMessageDto.getMessage() != null) {
            Label textLabel = new Label(notifyMessageDto.getMessage());
            textLabel.setTextAlignment(TextAlignment.CENTER);
            textLabel.setWrapText(true);
            textLabel.setMinHeight(Region.USE_PREF_SIZE);
            textLabel.setFont(Font.font(null, FontWeight.BOLD, 17));

            content.getChildren().add(textLabel);
        }

        String dateToString = Tools.dateTimeToString(Optional.ofNullable(notifyMessageDto.getCreate())
                .orElse(new Date()));

        Label timeLabel = new Label(dateToString);
        timeLabel.setTextAlignment(TextAlignment.CENTER);
        timeLabel.setWrapText(true);
        timeLabel.setFont(Font.font(null, FontWeight.BOLD, 15));

        content.getChildren().add(timeLabel);

        mainPane = content;

//        mainPane = Borders.wrap(content)
//                .lineBorder()
//                .thickness(2)
//                .innerPadding(0)
//                .radius(5, 5, 5, 5)
//                .color(Color.color(0.114, 0.161, 0.209))
//                .buildAll();

        viewVisit.accept(this);
    }

    @Override
    public Node getContent() {
        return mainPane;
    }

    @Override
    public void setText(String text) {

    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setType(NotificationType type) {

    }

    @Override
    public void setPosition(Pos position) {

    }

    @Override
    public void setNotifyAction(List<NotifyAction> notifyAction) {

    }

    @Override
    public void setDuration(Duration duration) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public boolean isShow() {
        return false;
    }

    @Override
    public void setOnClose(EventHandler<WindowEvent> actionEventConsumer) {

    }
}
