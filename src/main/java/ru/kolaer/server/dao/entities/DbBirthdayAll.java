package ru.kolaer.server.dao.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the db_birthday_all database table.
 * 
 */
@Entity
@Table(name="db_birthday_all")
public class DbBirthdayAll implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique=true, nullable=false)
	private short id;

	@Temporal(TemporalType.DATE)
	@Column(nullable=false)
	private Date birthday;
	
	@Column(name="organization", length=50)
	private String organization;
	
	@Column(name="category_unit", nullable=false, length=50)
	private String categoryUnit;

	@Column(nullable=false, length=100)
	private String departament;

	@Column(nullable=false, length=100)
	private String email;

	@Column(nullable=false, length=70)
	private String initials;

	@Column(name="mobile_phone", nullable=false)
	private String mobilePhone;

	@Column(nullable=false, length=40)
	private String phone;

	@Column(nullable=false, length=100)
	private String post;

	public DbBirthdayAll() {
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

	public String getDepartament() {
		return this.departament;
	}

	public void setDepartament(String departament) {
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

}