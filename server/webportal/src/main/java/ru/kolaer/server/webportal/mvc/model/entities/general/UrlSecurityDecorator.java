package ru.kolaer.server.webportal.mvc.model.entities.general;

import ru.kolaer.api.mvp.model.kolaerweb.webportal.UrlSecurity;
import ru.kolaer.api.mvp.model.kolaerweb.webportal.UrlSecurityBase;

import javax.persistence.*;
import java.util.List;

/**
 * Created by danilovey on 28.07.2016.
 * Структура URl из БД.
 */
@Entity
@Table(name = "url_security")
public class UrlSecurityDecorator implements UrlSecurity {
    private UrlSecurity urlSecurity;

    public UrlSecurityDecorator() {
        this.urlSecurity = new UrlSecurityBase();
    }

    public UrlSecurityDecorator(UrlSecurity urlSecurity) {
        this.urlSecurity = urlSecurity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    public int getId() {
        return this.urlSecurity.getId();
    }

    public void setId(int id) {
        this.urlSecurity.setId(id);
    }

    @Column(name = "url")
    public String getUrl() {
        return this.urlSecurity.getUrl();
    }

    public void setUrl(String url) {
        this.urlSecurity.setUrl(url);
    }

    @Column(name = "request_method", nullable = false)
    public String getRequestMethod() {
        return this.urlSecurity.getRequestMethod();
    }

    @Override
    public void setRequestMethod(String method) {
        this.urlSecurity.setRequestMethod(method);
    }

    @Column(name = "description")
    public String getDescription() {
        return this.urlSecurity.getDescription();
    }

    public void setDescription(String description) {
        this.urlSecurity.setDescription(description);
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "url_security_access", joinColumns=@JoinColumn(name="id_url"))
    @Column(name="name_role")
    public List<String> getAccesses() {
        return this.urlSecurity.getAccesses();
    }

    public void setAccesses(List<String> accesses) {
        this.urlSecurity.setAccesses(accesses);
    }
}