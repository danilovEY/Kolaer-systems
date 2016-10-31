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
	@Column(unique=true, nullable=false)
	public short getId() {
		return this.employeeOtherOrganization.getId();
	}

	public void setId(short id) {
		this.employeeOtherOrganization.setId(id);
	}

	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	public Date getBirthday() {
		return this.employeeOtherOrganization.getBirthday();
	}

	public void setBirthday(Date birthday) {
		this.employeeOtherOrganization.setBirthday(birthday);
	}

	@Column(name="category_unit", nullable=false, length=50)
	public String getCategoryUnit() {
		return this.employeeOtherOrganization.getCategoryUnit();
	}

	public void setCategoryUnit(String categoryUnit) {
		this.employeeOtherOrganization.setCategoryUnit(categoryUnit);
	}

	@Column(nullable=false, length=100)
	public String getDepartament() {
		return this.employeeOtherOrganization.getDepartament();
	}

	public void setDepartament(String departament) {
		this.employeeOtherOrganization.setDepartament(departament);
	}

	@Column(nullable=false, length=100)
	public String getEmail() {
		return this.employeeOtherOrganization.getEmail();
	}

	public void setEmail(String email) {
		this.employeeOtherOrganization.setEmail(email);
	}

	@Column(nullable=false, length=70)
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

	@Column(nullable=false, length=40)
	public String getPhone() {
		return this.employeeOtherOrganization.getPhone();
	}

	public void setPhone(String phone) {
		this.employeeOtherOrganization.setPhone(phone);
	}

	@Column(nullable=false, length=100)
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

	@Column(nullable=false, length=70)
	public String getvCard() {
		return this.employeeOtherOrganization.getvCard();
	}

	public void setvCard(String vCard) {
		this.employeeOtherOrganization.setvCard(vCard);
	}
}