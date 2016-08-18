package ru.kolaer.api.mvp.model.kolaerweb;

/**
 * Created by danilovey on 18.08.2016.
 */
public class NotifyMessageBase implements NotifyMessage {
    private String message;

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
