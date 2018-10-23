package ru.kolaer.server.webportal.microservice.employee.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import ru.kolaer.common.mvp.model.kolaerweb.EnumCategory;
import ru.kolaer.common.mvp.model.kolaerweb.EnumGender;
import ru.kolaer.server.webportal.common.entities.BaseEntity;
import ru.kolaer.server.webportal.microservice.post.entity.PostEntity;
import ru.kolaer.server.webportal.microservice.contact.entity.ContactEntity;
import ru.kolaer.server.webportal.microservice.typework.entity.TypeWorkEntity;
import ru.kolaer.server.webportal.microservice.department.entity.DepartmentEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Danilov on 24.07.2016.
 * Структура сотрудника в БД.
 */
@Entity
@Table(name = "employee")
@Data
public class EmployeeEntity implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "personnel_number", length = 100, nullable = false, unique = true)
    private Long personnelNumber;

    @Column(name = "initials", nullable = false)
    private String initials;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "second_name", nullable = false, length = 100)
    private String secondName;

    @Column(name = "third_name", nullable = false, length = 100)
    private String thirdName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 8, nullable = false)
    private EnumGender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "category_unit", length = 50)
    private EnumCategory category;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "post_id")
    private Long postId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable=false, updatable=false)
    private DepartmentEntity department;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable=false, updatable=false)
    private PostEntity post;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(name = "employment_date")
    @Temporal(TemporalType.DATE)
    private Date employmentDate;

    @Column(name = "dismissal_date")
    @Temporal(TemporalType.DATE)
    private Date dismissalDate;

    @Column(name = "photo", length = 300)
    private String photo;

    @Column(name = "contact_id")
    private Long contactId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", insertable=false, updatable=false)
    private ContactEntity contact;

    @Column(name = "harmfulness", nullable = false)
    private boolean harmfulness;

    @Column(name = "type_work_id")
    private Long typeWorkId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_work_id", insertable=false, updatable=false)
    private TypeWorkEntity typeWork;
}