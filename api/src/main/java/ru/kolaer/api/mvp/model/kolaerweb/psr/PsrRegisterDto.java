package ru.kolaer.api.mvp.model.kolaerweb.psr;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;

import java.util.Date;
import java.util.List;

/**
 * Created by danilovey on 29.07.2016.
 */
@Data
public class PsrRegisterDto implements BaseDto {
    private Long id;
    private String status;
    private EmployeeDto author;
    private EmployeeDto admin;
    private String name;
    private Date dateOpen;
    private Date dateClose;
    private String comment;
    private List<PsrAttachmentDto> attachments;
    private List<PsrStateDto> stateList;
}
