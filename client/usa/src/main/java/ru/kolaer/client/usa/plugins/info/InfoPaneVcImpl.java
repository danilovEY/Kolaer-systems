package ru.kolaer.client.usa.plugins.info;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.api.mvp.model.error.ServerExceptionMessage;
import ru.kolaer.api.system.impl.UniformSystemEditorKitSingleton;
import ru.kolaer.api.system.ui.NotifyAction;
import ru.kolaer.api.system.ui.StaticView;
import ru.kolaer.api.tools.Tools;
import ru.kolaer.client.usa.system.ui.NotificationPaneVc;
import ru.kolaer.client.usa.system.ui.UISystemUSImpl;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by danilovey on 13.02.2018.
 */
@Slf4j
public class InfoPaneVcImpl implements InfoPaneVc {
    private final NotificationPaneVc notificationPane = new NotificationPaneVc();
    private BorderPane mainPane;


    @Override
    public void initView(Consumer<InfoPaneVc> viewVisit) {
        mainPane = new BorderPane();

        ScrollPane scrollPaneUserNotify = new ScrollPane();
        scrollPaneUserNotify.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneUserNotify.setMinWidth(300);
        scrollPaneUserNotify.setPrefWidth(300);
        scrollPaneUserNotify.setMaxWidth(400);
        scrollPaneUserNotify.setFitToHeight(true);
        scrollPaneUserNotify.setFitToWidth(true);

        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(new BorderPane(), scrollPaneUserNotify);
        splitPane.setDividerPositions(1);

        mainPane.setCenter(splitPane);

        notificationPane.initView(notifyPane -> scrollPaneUserNotify.setContent(notifyPane.getContent()));

        UISystemUSImpl uiSystemUS = (UISystemUSImpl) UniformSystemEditorKitSingleton.getInstance().getUISystemUS();
        uiSystemUS.setStaticUS(this);
        uiSystemUS.setNotification(this);

        viewVisit.accept(this);
    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }

    @Override
    public void showSimpleNotify(String title, String text) {
        notificationPane.showSimpleNotify(title, text);
    }

    @Override
    public void showErrorNotify(String title, String text) {
        notificationPane.showErrorNotify(title, text);
    }

    @Override
    public void showWarningNotify(String title, String text) {
        notificationPane.showWarningNotify(title, text);
    }

    @Override
    public void showInformationNotify(String title, String text) {
        notificationPane.showInformationNotify(title, text);
    }

    @Override
    public void showInformationNotify(String title, String text, Duration duration) {
        notificationPane.showInformationNotify(title, text, duration);
    }

    @Override
    public void showSimpleNotify(String title, String text, Duration duration) {
        notificationPane.showSimpleNotify(title, text, duration);
    }

    @Override
    public void showSimpleNotify(String title, String text, Duration duration, Pos pos, List<NotifyAction> actions) {
        notificationPane.showSimpleNotify(title, text, duration, pos, actions);
    }

    @Override
    public void showSimpleNotify(String title, String text, Duration duration, List<NotifyAction> actions) {
        notificationPane.showSimpleNotify(title, text, duration, actions);
    }

    @Override
    public void showErrorNotify(String title, String text, List<NotifyAction> actions) {
        notificationPane.showErrorNotify(title, text, actions);
    }

    @Override
    public void showWarningNotify(String title, String text, List<NotifyAction> actions) {
        notificationPane.showWarningNotify(title, text, actions);
    }

    @Override
    public void showInformationNotify(String title, String text, Duration duration, Pos pos, List<NotifyAction> actions) {
        notificationPane.showInformationNotify(title, text, duration, pos, actions);
    }

    @Override
    public void showInformationNotify(String title, String text, Duration duration, List<NotifyAction> actions) {
        notificationPane.showInformationNotify(title, text, duration, actions);
    }

    @Override
    public void showErrorNotify(ServerExceptionMessage exceptionMessage) {
        notificationPane.showErrorNotify(exceptionMessage);
    }

    @Override
    public void showErrorNotify(Exception ex) {
        notificationPane.showErrorNotify(ex);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Ошибка в потоке: {}", t.getName(), e);
        Tools.runOnWithOutThreadFX(() -> notificationPane.showErrorNotify("Ошибка!", e.getLocalizedMessage()));
    }

    @Override
    public void addStaticView(StaticView staticView) {

    }

    @Override
    public void removeStaticView(StaticView staticView) {

    }
}
