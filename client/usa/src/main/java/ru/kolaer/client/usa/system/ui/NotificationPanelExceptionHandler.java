package ru.kolaer.client.usa.system.ui;

import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import ru.kolaer.common.dto.error.ServerExceptionMessage;
import ru.kolaer.common.mvp.view.BaseView;
import ru.kolaer.common.system.ui.NotificationUS;
import ru.kolaer.common.system.ui.NotifyAction;
import ru.kolaer.common.system.ui.StaticUS;
import ru.kolaer.common.system.ui.StaticView;
import ru.kolaer.common.tools.Tools;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by danilovey on 18.08.2016.
 */
@Slf4j
public class NotificationPanelExceptionHandler implements NotificationUS,
        BaseView<NotificationPanelExceptionHandler, Parent>, StaticUS,
        Thread.UncaughtExceptionHandler {
    private BorderPane mainPane;

    private NotificationPaneVc notificationPane;
    private StaticPaneVc staticPaneVc;

    public NotificationPanelExceptionHandler() {
        this.notificationPane = new NotificationPaneVc();
        this.staticPaneVc = new StaticPaneVc();
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
    public void setContent(Parent content) {

    }

    @Override
    public Parent getContent() {
        return this.mainPane;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Ошибка в потоке: {}", t.getName(), e);
        Tools.runOnWithOutThreadFX(() -> notificationPane.showErrorNotify("Ошибка!", e.getLocalizedMessage()));
    }

    @Override
    public void initView(Consumer<NotificationPanelExceptionHandler> viewVisit) {
        ScrollPane scrollPaneUserNotify = new ScrollPane();
        scrollPaneUserNotify.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneUserNotify.setMinWidth(300);
        scrollPaneUserNotify.setPrefWidth(300);
        scrollPaneUserNotify.setMaxWidth(400);
        scrollPaneUserNotify.setFitToHeight(true);
        scrollPaneUserNotify.setFitToWidth(true);

        ScrollPane scrollPaneStatic = new ScrollPane();
        scrollPaneStatic.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPaneStatic.setMinWidth(300);
        scrollPaneStatic.setMinHeight(100);
        scrollPaneStatic.setPrefWidth(300);
        scrollPaneStatic.setPrefHeight(100);
        scrollPaneStatic.setMaxWidth(400);
        //scrollPaneAdminNotify.setMaxHeight(400);
        scrollPaneStatic.setFitToHeight(true);
        scrollPaneStatic.setFitToWidth(true);

        notificationPane.initView(notifyPane -> scrollPaneUserNotify.setContent(notifyPane.getContent()));
        staticPaneVc.initView(staticPane -> scrollPaneStatic.setContent(staticPane.getContent()));


        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(scrollPaneStatic, scrollPaneUserNotify);
        splitPane.setDividerPositions(0.6);
        splitPane.setOrientation(Orientation.VERTICAL);

        this.mainPane = new BorderPane(splitPane);
        this.mainPane.setMinWidth(300);
        this.mainPane.setPrefWidth(300);
        this.mainPane.setMaxWidth(400);

        viewVisit.accept(this);
    }

    @Override
    public void addStaticView(StaticView staticView) {
        staticPaneVc.addStaticView(staticView);
    }

    @Override
    public void removeStaticView(StaticView staticView) {
        staticPaneVc.removeStaticView(staticView);
    }
}
