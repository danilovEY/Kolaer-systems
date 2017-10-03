package ru.kolaer.server.webportal.mvc.model.entities.birthday;

import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganization;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganizationBase;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name="employee_other_organization")
public class EmployeeOtherOrganizationDecorator implements EmployeeOtherOrganization {
	private EmployeeOtherOrganization employeeOtherOrganization;

	public EmployeeOtherOrganizationDecorator(EmployeeOtherOrganization employeeOtherOrganization) {
		this.employeeOtherOrganization = employeeOtherOrganization;
	}

	public EmployeeOtherOrganizationDecorator() {
		this(new EmployeeOtherOrganizationBase());
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	public short getId() {
		return this.employeeOtherOrganization.getId();
	}

	public void setId(short id) {
		this.employeeOtherOrganization.setId(id);
	}

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	public Date getBirthday() {
		return this.employeeOtherOrganization.getBirthday();
	}

	public void setBirthday(Date birthday) {
		this.employeeOtherOrganization.setBirthday(birthday);
	}

	@Column(name="category_unit", length=50)
	public String getCategoryUnit() {
		return this.employeeOtherOrganization.getCategoryUnit();
	}

	public void setCategoryUnit(String categoryUnit) {
		this.employeeOtherOrganization.setCategoryUnit(categoryUnit);
	}

	@Column
	public String getDepartment() {
		return this.employeeOtherOrganization.getDepartment();
	}

	public void setDepartment(String departament) {
		this.employeeOtherOrganization.setDepartment(departament);
	}

	@Column(length=100)
	public String getEmail() {
		return this.employeeOtherOrganization.getEmail();
	}

	public void setEmail(String email) {
		this.employeeOtherOrganization.setEmail(email);
	}

	@Column(length=70)
	public String getInitials() {
		return this.employeeOtherOrganization.getInitials();
	}

	public void setInitials(String initials) {
		this.employeeOtherOrganization.setInitials(initials);
	}

	@Column(name="mobile_phone", nullable=false)
	public String getMobilePhone() {
		return this.employeeOtherOrganization.getMobilePhone();
	}

	public void setMobilePhone(String mobilePhone) {
		this.employeeOtherOrganization.setMobilePhone(mobilePhone);
	}

	@Column(length=40)
	public String getPhone() {
		return this.employeeOtherOrganization.getPhone();
	}

	public void setPhone(String phone) {
		this.employeeOtherOrganization.setPhone(phone);
	}

	@Column(length=100)
	public String getPost() {
		return this.employeeOtherOrganization.getPost();
	}

	public void setPost(String post) {
		this.employeeOtherOrganization.setPost(post);
	}

	@Column(name="organization", length=50)
	public String getOrganization() {
		return this.employeeOtherOrganization.getOrganization();
	}

	public void setOrganization(String organization) {
		this.employeeOtherOrganization.setOrganization(organization);
	}

	@Column(length=70)
	public String getPhoto() {
		return this.employeeOtherOrganization.getPhoto();
	}

	public void setPhoto(String vCard) {
		this.employeeOtherOrganization.setPhoto(vCard);
	}
}