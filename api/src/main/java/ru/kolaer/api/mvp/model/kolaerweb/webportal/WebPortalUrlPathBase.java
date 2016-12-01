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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WebPortalUrlPathBase that = (WebPortalUrlPathBase) o;

        if (id != that.id) return false;
        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (requestMethod != null ? !requestMethod.equals(that.requestMethod) : that.requestMethod != null)
            return false;
        return accessed != null ? accessed.equals(that.accessed) : that.accessed == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (requestMethod != null ? requestMethod.hashCode() : 0);
        result = 31 * result + (accessed != null ? accessed.hashCode() : 0);
        return result;
    }
}
