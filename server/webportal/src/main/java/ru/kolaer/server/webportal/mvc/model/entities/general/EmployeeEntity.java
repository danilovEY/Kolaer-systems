package ru.kolaer.server.webportal.mvc.model.entities.general;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.EnumGender;
import ru.kolaer.server.webportal.mvc.model.entities.BaseEntity;

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

    @Column(name = "gender", length = 8, nullable = false)
    private EnumGender gender;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "post_id")
    private Long postId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", insertable=false, updatable=false)
    private DepartmentEntity department;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable=false, updatable=false)
    private PostEntity post;

    @Column(name = "work_phone_number")
    private String workPhoneNumber;

    @Column(name = "home_phone_number")
    private String homePhoneNumber;

    @Column(name = "birthday")
    @Temporal(TemporalType.DATE)
    private Date birthday;

    @Column(name = "employment_date")
    @Temporal(TemporalType.DATE)
    private Date employmentDate;

    @Column(name = "dismissal_date")
    @Temporal(TemporalType.DATE)
    private Date dismissalDate;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "photo", length = 300)
    private String photo;
}