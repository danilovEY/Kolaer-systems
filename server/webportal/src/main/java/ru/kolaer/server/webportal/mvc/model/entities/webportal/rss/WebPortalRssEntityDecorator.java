package ru.kolaer.server.webportal.mvc.model.entities.webportal.rss;

import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntityBase;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.rss.WebPortalRssEntity;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.rss.WebPortalRssEntityBase;
import ru.kolaer.server.webportal.mvc.model.entities.general.GeneralAccountsEntityDecorator;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by Danilov on 24.07.2016.
 * Структура новости из БД.
 */
@Entity
@Table(name = "webportal_rss")
public class WebPortalRssEntityDecorator implements WebPortalRssEntity {
    private WebPortalRssEntity webPortalRssEntity;

    public WebPortalRssEntityDecorator() {
        this.webPortalRssEntity = new WebPortalRssEntityBase();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public int getId() {
        return this.webPortalRssEntity.getId();
    }

    public void setId(int id) {
        this.webPortalRssEntity.setId(id);
    }

    @OneToOne(targetEntity = GeneralAccountsEntityDecorator.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_account")
    public GeneralAccountsEntity getUser() {
        return this.webPortalRssEntity.getUser();
    }

    public void setUser(GeneralAccountsEntity user) {
        this.webPortalRssEntity.setUser(user);
    }

    @Column(name = "title")
    public String getTitle() {
        return this.webPortalRssEntity.getTitle();
    }

    public void setTitle(String title) {
        this.webPortalRssEntity.setTitle(title);
    }

    @Column(name = "content")
    public String getContent() {
        return this.webPortalRssEntity.getContent();
    }

    public void setContent(String content) {
        this.webPortalRssEntity.setContent(content);
    }

    @Column(name = "date")
    public Date getDate() {
        return this.webPortalRssEntity.getDate();
    }

    public void setDate(Date date) {
        this.webPortalRssEntity.setDate(date);
    }

    @Column(name = "hide")
    public boolean isHide() {
        return this.webPortalRssEntity.isHide();
    }

    public void setHide(boolean hide) {
        this.webPortalRssEntity.setHide(hide);
    }

    @Column(name = "priority")
    public int getPriority() {
        return this.webPortalRssEntity.getPriority();
    }

    public void setPriority(int priority) {
        this.webPortalRssEntity.setPriority(priority);
    }

    @Override
    public boolean equals(Object o) {
        return this.webPortalRssEntity.equals(o);
    }

    @Override
    public int hashCode() {
        return this.webPortalRssEntity.hashCode();
    }
}