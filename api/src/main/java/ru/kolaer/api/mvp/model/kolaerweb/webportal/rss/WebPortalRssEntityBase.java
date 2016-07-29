package ru.kolaer.api.mvp.model.kolaerweb.webportal.rss;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntityBase;

import java.sql.Date;

/**
 * Created by Danilov on 24.07.2016.
 * Структура новости из БД.
 */
public class WebPortalRssEntityBase implements WebPortalRssEntity {
    private int id;
    private String title;
    private String content;
    private Date date;
    private boolean isHide;
    private int priority;
    private GeneralAccountsEntity user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GeneralAccountsEntity getUser() {
        return user;
    }

    public void setUser(GeneralAccountsEntity user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        this.isHide = hide;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebPortalRssEntityBase that = (WebPortalRssEntityBase) o;

        if (id != that.id) return false;
        if (user != that.user) return false;
        if (isHide != that.isHide) return false;
        if (priority != that.priority) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (content != null ? !content.equals(that.content) : that.content != null) return false;
        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) id;
        //result = 31 * result + (int) user;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (isHide ? 1 : 0);
        result = 31 * result + priority;
        return result;
    }
}
