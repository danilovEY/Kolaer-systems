package ru.kolaer.server.webportal.model.entity.birthday;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.EnumCategory;
import ru.kolaer.api.mvp.model.kolaerweb.EnumGender;
import ru.kolaer.server.webportal.model.entity.BaseEntity;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="employee_other_organization")
@Data
public class EmployeeOtherOrganizationEntity implements BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "birthday")
	@Temporal(TemporalType.DATE)
	private Date birthday;

	@Column(name = "gender", nullable = false)
	@Enumerated(EnumType.STRING)
	private EnumGender gender;

	@Enumerated(EnumType.STRING)
	@Column(name = "category_unit", length = 50)
	private EnumCategory category;

	@Column(name = "department")
	private String department;

	@Column(name = "email", length=100)
	private String email;

	@Column(name = "initials", nullable = false)
	private String initials;

	@Column(name = "first_name", length=100, nullable = false)
	private String firstName;

	@Column(name = "second_name",length=100, nullable = false)
	private String secondName;

	@Column(name = "third_name", length=100, nullable = false)
	private String thirdName;

	@Column(name="work_phone_number")
	private String workPhoneNumber;

	@Column(name = "post")
	private String post;

	@Column(name="organization")
	private String organization;
}