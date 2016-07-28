package ru.kolaer.server.webportal.mvc.model.entities.webportal;

import javax.persistence.*;

/**
 * Created by danilovey on 28.07.2016.
 */
@Entity
@Table(name = "webportal_url_paths")
public class WebPortalUrlPath {
    private int id;
    private String url;
    private String description;
    private boolean accessAll;
    private boolean accessSuperAdmin;
    private boolean accessAdmin;
    private boolean accessUser;
    private boolean accessAnonymous;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "access_all")
    public boolean isAccessAll() {
        return accessAll;
    }

    public void setAccessAll(boolean accessAll) {
        this.accessAll = accessAll;
    }

    @Column(name = "access_super_admin")
    public boolean isAccessSuperAdmin() {
        return accessSuperAdmin;
    }

    public void setAccessSuperAdmin(boolean accessSuperAdmin) {
        this.accessSuperAdmin = accessSuperAdmin;
    }

    @Column(name = "access_admin")
    public boolean isAccessAdmin() {
        return accessAdmin;
    }

    public void setAccessAdmin(boolean accessAdmin) {
        this.accessAdmin = accessAdmin;
    }

    @Column(name = "access_user")
    public boolean isAccessUser() {
        return accessUser;
    }

    public void setAccessUser(boolean accessUser) {
        this.accessUser = accessUser;
    }

    @Column(name = "access_anonymous")
    public boolean isAccessAnonymous() {
        return accessAnonymous;
    }

    public void setAccessAnonymous(boolean accessAnonymous) {
        this.accessAnonymous = accessAnonymous;
    }
}
