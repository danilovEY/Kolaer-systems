/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kolaer.server.dao.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Danilov
 */
@Entity
@Table(name = "db_data_all", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbDataAll implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8116697743480287748L;

	@Id
	@Column(name="person_number", unique=true, nullable=false)
	private int personNumber;

	@Temporal(TemporalType.DATE)
	private Date birthday;

	@Column(name="category_unit", nullable=false, length=1)
	private String categoryUnit;

	@Column(name="controlled_access_area", nullable=false, length=1)
	private String controlledAccessArea;

	@Temporal(TemporalType.DATE)
	@Column(name="date_invite_work")
	private Date dateInviteWork;

	@Column(length=100)
	private String departament;

	@Column(name="departament_abbreviated", length=100)
	private String departamentAbbreviated;

	@Column(length=100)
	private String email;

	@Column(name="email_password", length=20)
	private String emailPassword;

	@Column(nullable=false, length=1)
	private String gender;

	@Column(name="id_departament")
	private int idDepartament;

	@Column(nullable=false, length=70)
	private String initials;

	@Column(length=50)
	private String login;

	@Column(name="medcontrol_value", nullable=false, length=1)
	private String medcontrolValue;

	@Temporal(TemporalType.DATE)
	@Column(name="medical_view")
	private Date medicalView;

	@Column(name="mobile_phone", length=10)
	private String mobilePhone;

	@Column(nullable=false, length=50)
	private String name;

	@Column(name="num_certificate", length=40)
	private String numCertificate;

	@Column(length=40)
	private String num_certificate_GE;

	@Column(length=5)
	private String pager;

	@Column(length=50)
	private String password;

	@Column(nullable=false, length=50)
	private String patronymic;

	@Column(length=40)
	private String phone;

	@Column(nullable=false, length=100)
	private String post;

	@Column(name="privilege_holiday", nullable=false, length=1)
	private String privilegeHoliday;

	@Column(length=20)
	private String room;

	@Column(length=255)
	private String specialization;

	@Column(nullable=false, length=50)
	private String surname;

	@Column(nullable=false, length=70)
	private String vCard;

	@Column(nullable=false, length=1)
	private String visible;

	public DbDataAll() {
	}

	public int getPersonNumber() {
		return this.personNumber;
	}

	public void setPersonNumber(int personNumber) {
		this.personNumber = personNumber;
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

	public String getControlledAccessArea() {
		return this.controlledAccessArea;
	}

	public void setControlledAccessArea(String controlledAccessArea) {
		this.controlledAccessArea = controlledAccessArea;
	}

	public Date getDateInviteWork() {
		return this.dateInviteWork;
	}

	public void setDateInviteWork(Date dateInviteWork) {
		this.dateInviteWork = dateInviteWork;
	}

	public String getDepartament() {
		return this.departament;
	}

	public void setDepartament(String departament) {
		this.departament = departament;
	}

	public String getDepartamentAbbreviated() {
		return this.departamentAbbreviated;
	}

	public void setDepartamentAbbreviated(String departamentAbbreviated) {
		this.departamentAbbreviated = departamentAbbreviated;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailPassword() {
		return this.emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getIdDepartament() {
		return this.idDepartament;
	}

	public void setIdDepartament(int idDepartament) {
		this.idDepartament = idDepartament;
	}

	public String getInitials() {
		return this.initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getMedcontrolValue() {
		return this.medcontrolValue;
	}

	public void setMedcontrolValue(String medcontrolValue) {
		this.medcontrolValue = medcontrolValue;
	}

	public Date getMedicalView() {
		return this.medicalView;
	}

	public void setMedicalView(Date medicalView) {
		this.medicalView = medicalView;
	}

	public String getMobilePhone() {
		return this.mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumCertificate() {
		return this.numCertificate;
	}

	public void setNumCertificate(String numCertificate) {
		this.numCertificate = numCertificate;
	}

	public String getNum_certificate_GE() {
		return this.num_certificate_GE;
	}

	public void setNum_certificate_GE(String num_certificate_GE) {
		this.num_certificate_GE = num_certificate_GE;
	}

	public String getPager() {
		return this.pager;
	}

	public void setPager(String pager) {
		this.pager = pager;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPatronymic() {
		return this.patronymic;
	}

	public void setPatronymic(String patronymic) {
		this.patronymic = patronymic;
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

	public String getPrivilegeHoliday() {
		return this.privilegeHoliday;
	}

	public void setPrivilegeHoliday(String privilegeHoliday) {
		this.privilegeHoliday = privilegeHoliday;
	}

	public String getRoom() {
		return this.room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getSpecialization() {
		return this.specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getVCard() {
		return this.vCard;
	}

	public void setVCard(String vCard) {
		this.vCard = vCard;
	}

	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String visible) {
		this.visible = visible;
	}


    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbDataAll[ personNumber=" + personNumber + " ]";
    }
    
}
