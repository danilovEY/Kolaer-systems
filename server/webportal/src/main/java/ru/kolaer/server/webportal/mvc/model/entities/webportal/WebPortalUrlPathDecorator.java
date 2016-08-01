package ru.kolaer.server.webportal.mvc.model.entities.webportal;

import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPath;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPathBase;

import javax.persistence.*;

/**
 * Created by danilovey on 28.07.2016.
 * Структура URl из БД.
 */
@Entity
@Table(name = "webportal_url_paths")
public class WebPortalUrlPathDecorator implements WebPortalUrlPath {
    private WebPortalUrlPath webPortalUrlPath;

    public WebPortalUrlPathDecorator() {
        this.webPortalUrlPath = new WebPortalUrlPathBase();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return this.webPortalUrlPath.getId();
    }

    public void setId(int id) {
        this.webPortalUrlPath.setId(id);
    }

    @Column(name = "url")
    public String getUrl() {
        return this.webPortalUrlPath.getUrl();
    }

    public void setUrl(String url) {
        this.webPortalUrlPath.setUrl(url);
    }

    @Column(name = "description")
    public String getDescription() {
        return this.webPortalUrlPath.getDescription();
    }

    public void setDescription(String description) {
        this.webPortalUrlPath.setDescription(description);
    }

    @Column(name = "access_all")
    public boolean isAccessAll() {
        return this.webPortalUrlPath.isAccessAll();
    }

    public void setAccessAll(boolean accessAll) {
        this.webPortalUrlPath.setAccessAll(accessAll);
    }

    @Column(name = "access_super_admin")
    public boolean isAccessSuperAdmin() {
        return this.webPortalUrlPath.isAccessSuperAdmin();
    }

    public void setAccessSuperAdmin(boolean accessSuperAdmin) {
        this.webPortalUrlPath.setAccessSuperAdmin(accessSuperAdmin);
    }

    @Column(name = "access_admin")
    public boolean isAccessAdmin() {
        return this.webPortalUrlPath.isAccessAdmin();
    }

    public void setAccessAdmin(boolean accessAdmin) {
        this.webPortalUrlPath.setAccessAdmin(accessAdmin);
    }

    @Column(name = "access_user")
    public boolean isAccessUser() {
        return this.webPortalUrlPath.isAccessUser();
    }

    public void setAccessUser(boolean accessUser) {
        this.webPortalUrlPath.setAccessUser(accessUser);
    }

    @Column(name = "access_anonymous")
    public boolean isAccessAnonymous() {
        return this.webPortalUrlPath.isAccessAnonymous();
    }

    public void setAccessAnonymous(boolean accessAnonymous) {
        this.webPortalUrlPath.setAccessAnonymous(accessAnonymous);
    }
}