package ru.kolaer.api.mvp.model.kolaerweb.jpac;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;

import java.util.Date;

/**
 * Created by danilovey on 06.09.2016.
 */
@Data
public class ViolationDto implements BaseDto {
    private Long id;
    private String violation;
    private String todo;
    private Date startMaking;
    private Date dateLimitElimination;
    private Date dateEndElimination;
    private EmployeeDto writer;
    private EmployeeDto executor;
    private boolean effective;
    private StageEnum stage;
    private TypeViolationDto type;
    private JournalViolationDto journalViolation;
}
