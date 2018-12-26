package ru.kolaer.server.holiday.model.entity;

import lombok.Data;
import ru.kolaer.common.dto.kolaerweb.TypeDay;
import ru.kolaer.server.core.model.entity.DefaultEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

/**
 * Created by danilovey on 31.10.2016.
 */
@Entity
@Table(name = "holiday")
@Data
public class HolidayEntity extends DefaultEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "holiday_date", nullable = false, unique = true)
    private LocalDate holidayDate;

    @Column(name = "holiday_type", nullable = false)
    private TypeDay holidayType;
}
