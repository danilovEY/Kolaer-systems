package ru.kolaer.server.webportal.model.entity.general;

import lombok.Data;
import ru.kolaer.server.webportal.model.entity.BaseEntity;

import javax.persistence.*;

/**
 * Created by danilovey on 28.07.2016.
 * Структура URl из БД.
 */
@Entity
@Table(name = "url_security")
@Data
public class UrlSecurityEntity implements BaseEntity {

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

    @Column(name="access_oit", nullable = false)
    private boolean accessOit;

    @Column(name="access_all", nullable = false)
    private boolean accessAll;

    @Column(name="access_user", nullable = false)
    private boolean accessUser = true;

    @Column(name="access_ok", nullable = false)
    private boolean accessOk;

    @Column(name = "access_vacation_admin", nullable = false)
    private boolean accessVacationAdmin;

    @Column(name = "access_vacation_dep_edit", nullable = false)
    private boolean accessVacationDepEdit;

    @Column(name = "access_type_work", nullable = false)
    private boolean accessTypeWork;
}