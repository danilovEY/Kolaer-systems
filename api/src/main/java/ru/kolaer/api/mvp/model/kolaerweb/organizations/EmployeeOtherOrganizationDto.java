package ru.kolaer.api.mvp.model.kolaerweb.organizations;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.api.mvp.model.kolaerweb.EnumCategory;
import ru.kolaer.api.mvp.model.kolaerweb.EnumGender;

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