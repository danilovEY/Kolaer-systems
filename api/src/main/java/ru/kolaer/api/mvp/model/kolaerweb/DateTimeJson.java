package ru.kolaer.api.mvp.model.kolaerweb;

/**
 * Created by danilovey on 25.08.2016.
 */
public class DateTimeJson {
    private String date;
    private String time;

    public DateTimeJson(){

    }

    public DateTimeJson(String date, String time) {
        this.date = date;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String data) {
        this.date = data;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return this.date + " | " + this.time;
    }
}
