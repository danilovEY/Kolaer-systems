package ru.kolaer.api.mvp.model.kolaerweb;

/**
 * Created by danilovey on 25.08.2016.
 */
public class DateTimeJson {
    private String data;
    private String time;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return this.data + "|" + this.time;
    }
}
