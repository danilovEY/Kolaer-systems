package ru.kolaer.client.psr.mvp.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import ru.kolaer.api.mvp.view.VComponentUI;

/**
 * Created by danilovey on 01.08.2016.
 */
public interface VMainPane extends VComponentUI, InitializationView {
    void loginAction(EventHandler<ActionEvent> event);
    EventHandler<ActionEvent> getLoginAction();
    void logoutAction(EventHandler<ActionEvent> event);
    void createPsrAction(EventHandler<ActionEvent> event);
    void setUserName(String userName);
    void setEnableCreatePstMenuItem(boolean enable);
}
