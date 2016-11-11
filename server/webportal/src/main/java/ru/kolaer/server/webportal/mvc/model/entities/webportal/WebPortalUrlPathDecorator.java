package ru.kolaer.server.webportal.mvc.model.entities.webportal;

import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPath;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.WebPortalUrlPathBase;

import javax.persistence.*;
import java.util.List;

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

    public WebPortalUrlPathDecorator(WebPortalUrlPath webPortalUrlPath) {
        this.webPortalUrlPath = webPortalUrlPath;
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

    @Column(name = "request_method", nullable = false)
    public String getRequestMethod() {
        return this.webPortalUrlPath.getRequestMethod();
    }

    @Override
    public void setRequestMethod(String method) {
        this.webPortalUrlPath.setRequestMethod(method);
    }

    @Column(name = "description")
    public String getDescription() {
        return this.webPortalUrlPath.getDescription();
    }

    public void setDescription(String description) {
        this.webPortalUrlPath.setDescription(description);
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "url_access", joinColumns=@JoinColumn(name="id_url"))
    @Column(name="access_name")
    public List<String> getAccesses() {
        return this.webPortalUrlPath.getAccesses();
    }

    public void setAccesses(List<String> accesses) {
        this.webPortalUrlPath.setAccesses(accesses);
    }
}