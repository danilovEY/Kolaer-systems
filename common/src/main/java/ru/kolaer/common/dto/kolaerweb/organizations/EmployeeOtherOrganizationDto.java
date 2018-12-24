package ru.kolaer.common.dto.kolaerweb.organizations;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;
import ru.kolaer.common.dto.kolaerweb.EnumCategory;
import ru.kolaer.common.dto.kolaerweb.EnumGender;

import java.util.Date;

@Data
public class EmployeeOtherOrganizationDto implements BaseDto {
	private static final long serialVersionUID = -9086880039708838378L;

	private Long id;
	private String organization;
	private Date birthday;
	private EnumCategory category;
	private String department;
	private String email;
	private String initials;
	private String firstName;
	private String secondName;
	private String thirdName;
	private String mobilePhone;
	private String post;
	private String photo;
	private String workPhoneNumber;
	private EnumGender gender;
}