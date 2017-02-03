package ru.kolaer.api.mvp.model.kolaerweb.organizations;

import java.util.Date;

public class EmployeeOtherOrganizationBase implements EmployeeOtherOrganization {
	private static final long serialVersionUID = -9086880039708838378L;

	private short id;
	private String organization;
	private Date birthday;
	private String categoryUnit;
	private String departament;
	private String email;
	private String initials;
	private String mobilePhone;
	private String phone;
	private String post;
	private String vCard;

	public EmployeeOtherOrganizationBase() {
	}

	public short getId() {
		return this.id;
	}

	public void setId(short id) {
		this.id = id;
	}

	public Date getBirthday() {
		return this.birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getCategoryUnit() {
		return this.categoryUnit;
	}

	public void setCategoryUnit(String categoryUnit) {
		this.categoryUnit = categoryUnit;
	}

	public String getDepartment() {
		return this.departament;
	}

	public void setDepartment(String departament) {
		this.departament = departament;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getInitials() {
		return this.initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPost() {
		return this.post;
	}

	public void setPost(String post) {
		this.post = post;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getPhoto() {
		return vCard;
	}

	public void setPhoto(String vCard) {
		this.vCard = vCard;
	}


}