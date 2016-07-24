package ru.kolaer.server.webportal.mvc.model.entities.rss;

import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralUsersEntity;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by Danilov on 24.07.2016.
 */
@Entity
@Table(name = "webportal_rss", schema = "application_db", catalog = "")
public class WebPortalRssEntity {
    private short id;
    private GeneralUsersEntity user;
    private String title;
    private String contentRss;
    private Date rssTime;
    private boolean rssHide;
    private int priority;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public short getId() {
        return id;
    }

    public void setId(short id) {
        this.id = id;
    }

    @Basic
    @OneToOne(cascade = CascadeType.ALL)
    public GeneralUsersEntity getUser() {
        return user;
    }

    public void setUser(GeneralUsersEntity user) {
        this.user = user;
    }

    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "content_rss")
    public String getContentRss() {
        return contentRss;
    }

    public void setContentRss(String contentRss) {
        this.contentRss = contentRss;
    }

    @Basic
    @Column(name = "rss_time")
    public Date getRssTime() {
        return rssTime;
    }

    public void setRssTime(Date rssTime) {
        this.rssTime = rssTime;
    }

    @Basic
    @Column(name = "rss_hide")
    public boolean isRssHide() {
        return rssHide;
    }

    public void setRssHide(boolean rssHide) {
        this.rssHide = rssHide;
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
        if (rssHide != that.rssHide) return false;
        if (priority != that.priority) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (contentRss != null ? !contentRss.equals(that.contentRss) : that.contentRss != null) return false;
        if (rssTime != null ? !rssTime.equals(that.rssTime) : that.rssTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) id;
        //result = 31 * result + (int) user;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (contentRss != null ? contentRss.hashCode() : 0);
        result = 31 * result + (rssTime != null ? rssTime.hashCode() : 0);
        result = 31 * result + (rssHide ? 1 : 0);
        result = 31 * result + priority;
        return result;
    }
}
