package ru.kolaer.server.employee.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.common.dto.employee.EnumCategory;
import ru.kolaer.common.dto.employee.EnumGender;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Danilov on 24.07.2016.
 * Структура сотрудника в БД.
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "employee")
@Data
public class EmployeeEntity extends DefaultEntity {

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

    @Column(name = "harmfulness", nullable = false)
    private boolean harmfulness;

    @Column(name = "type_work_id")
    private Long typeWorkId;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_work_id", insertable=false, updatable=false)
    private TypeWorkEntity typeWork;

    @Column(name = "contract_number", length = 20)
    private String contractNumber;
}