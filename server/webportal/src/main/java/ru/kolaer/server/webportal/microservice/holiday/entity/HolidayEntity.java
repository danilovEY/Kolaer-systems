package ru.kolaer.server.webportal.microservice.holiday.entity;

import lombok.Data;
import ru.kolaer.common.mvp.model.kolaerweb.TypeDay;
import ru.kolaer.server.webportal.common.entities.BaseEntity;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Created by danilovey on 31.10.2016.
 */
@Entity
@Table(name = "holiday")
@Data
public class HolidayEntity implements BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "holiday_date", nullable = false, unique = true)
    private LocalDate holidayDate;

    @Column(name = "holiday_type", nullable = false)
    private TypeDay holidayType;
}
