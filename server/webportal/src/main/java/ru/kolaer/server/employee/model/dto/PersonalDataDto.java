package ru.kolaer.server.employee.model.dto;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.common.dto.DefaultDto;

import java.time.LocalDate;

@Getter
@Setter
public class PersonalDataDto extends DefaultDto {
    private Long employeeId;
    private String maritalStatus;
    private String phoneNumber;
    private String addressRegistration;
    private String addressResidential;
    private String placeOfBirth;
    private String disabilityGroup;
    private LocalDate disabilityDate;
}
