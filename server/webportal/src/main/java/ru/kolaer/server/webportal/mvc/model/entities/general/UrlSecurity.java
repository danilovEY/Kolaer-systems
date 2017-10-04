package ru.kolaer.server.webportal.mvc.model.entities.general;

import lombok.Data;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

import javax.persistence.*;
import java.util.List;

/**
 * Created by danilovey on 28.07.2016.
 * Структура URl из БД.
 */
@Entity
@Table(name = "url_security")
@Data
public class UrlSecurity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "url")
    private String url;

    @Column(name = "request_method", nullable = false)
    private String requestMethod;

    @Column(name = "description")
    private String description;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "url_security_access", joinColumns=@JoinColumn(name="id_url"))
    @Column(name="name_role")
    public List<String> accesses;
}