package ru.kolaer.api.mvp.model.kolaerweb.webportal;

/**
 * Created by danilovey on 28.07.2016.
 * Структура URl из БД.
 */
public class WebPortalUrlPathBase implements WebPortalUrlPath {
    private int id;
    private String url;
    private String description;
    /**Имеют ли доступ к URL все пользователи*/
    private boolean accessAll;
    private boolean accessSuperAdmin;
    private boolean accessAdmin;
    private boolean accessUser;
    private boolean accessAnonymous;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAccessAll() {
        return accessAll;
    }

    public void setAccessAll(boolean accessAll) {
        this.accessAll = accessAll;
    }

    public boolean isAccessSuperAdmin() {
        return accessSuperAdmin;
    }

    public void setAccessSuperAdmin(boolean accessSuperAdmin) {
        this.accessSuperAdmin = accessSuperAdmin;
    }

    public boolean isAccessAdmin() {
        return accessAdmin;
    }

    public void setAccessAdmin(boolean accessAdmin) {
        this.accessAdmin = accessAdmin;
    }

    public boolean isAccessUser() {
        return accessUser;
    }

    public void setAccessUser(boolean accessUser) {
        this.accessUser = accessUser;
    }

    public boolean isAccessAnonymous() {
        return accessAnonymous;
    }

    public void setAccessAnonymous(boolean accessAnonymous) {
        this.accessAnonymous = accessAnonymous;
    }
}
