package ru.kolaer.server.webportal.mvc.model.entities.webportal.rss;

import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.rss.WebPortalRssEntity;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.rss.WebPortalRssEntityBase;
import ru.kolaer.server.webportal.mvc.model.entities.general.EmployeeEntityDecorator;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by Danilov on 24.07.2016.
 * Структура новости из БД.
 */
//@Entity
//@Table(name = "webportal_rss")
public class WebPortalRssEntityDecorator implements WebPortalRssEntity {
    private WebPortalRssEntity webPortalRssEntity;

    public WebPortalRssEntityDecorator() {
        this.webPortalRssEntity = new WebPortalRssEntityBase();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return this.webPortalRssEntity.getId();
    }

    public void setId(int id) {
        this.webPortalRssEntity.setId(id);
    }

    @OneToOne(targetEntity = EmployeeEntityDecorator.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_employee")
    public EmployeeEntity getUser() {
        return this.webPortalRssEntity.getUser();
    }

    public void setUser(EmployeeEntity user) {
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