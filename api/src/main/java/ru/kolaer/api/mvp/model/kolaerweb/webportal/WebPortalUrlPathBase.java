package ru.kolaer.api.mvp.model.kolaerweb.webportal;

import java.util.List;

/**
 * Created by danilovey on 28.07.2016.
 * Структура URl из БД.
 */
public class WebPortalUrlPathBase implements WebPortalUrlPath {
    private int id;
    private String url;
    private String description;
    private String requestMethod;
    /**Имеют ли доступ к URL все пользователи*/
    private List<String> accessed;

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

    @Override
    public String getRequestMethod() {
        return this.requestMethod;
    }

    @Override
    public void setRequestMethod(String method) {
        this.requestMethod = method;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getAccesses() {
        return this.accessed;
    }

    public void setAccesses(List<String> accesses) {
        this.accessed = accesses;
    }
}
