package ru.kolaer.server.webportal.mvc.model.entities.rss;

import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralAccountsEntity;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by Danilov on 24.07.2016.
 */
@Entity
@Table(name = "webportal_rss")
public class WebPortalRssEntity {
    private int id;
    private String title;
    private String content;
    private Date date;
    private boolean isHide;
    private int priority;
    private GeneralAccountsEntity user;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_account")
    public GeneralAccountsEntity getUser() {
        return user;
    }

    public void setUser(GeneralAccountsEntity user) {
        this.user = user;
    }

    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "date")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Basic
    @Column(name = "hide")
    public boolean isHide() {
        return isHide;
    }

    public void setHide(boolean hide) {
        this.isHide = hide;
    }

    @Basic
    @Column(name = "priority")
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

        WebPortalRssEntity that = (WebPortalRssEntity) o;

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
