/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kolaer.server.dao.entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Danilov
 */
@MappedSuperclass
@Table(name = "db_data_all", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbDataAll implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "person_number")
    private Short personNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    private String initials;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    private String surname;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    private String patronymic;
    @Size(max = 100)
    private String departament;
    @Size(max = 100)
    @Column(name = "departament_abbreviated")
    private String departamentAbbreviated;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String post;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    private String gender;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Недопустимый адрес электронной почты")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    private String email;
    @Size(max = 20)
    @Column(name = "email_password")
    private String emailPassword;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Недопустимый формат номера телефона/факса (должен иметь формат xxx-xxx-xxxx)")//if the field contains phone or fax number consider using this annotation to enforce field validation
    @Size(max = 40)
    private String phone;
    @Size(max = 10)
    @Column(name = "mobile_phone")
    private String mobilePhone;
    @Size(max = 5)
    private String pager;
    @Size(max = 20)
    private String room;
    @Size(max = 50)
    private String login;
    @Size(max = 50)
    private String password;
    @Size(max = 40)
    @Column(name = "PC_name")
    private String pCname;
    @Column(name = "IP_addr")
    private Short iPaddr;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 70)
    private String vCard;
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @Column(name = "date_invite_work")
    @Temporal(TemporalType.DATE)
    private Date dateInviteWork;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    private String visible;
    @Size(max = 6)
    @Column(name = "privilege_auto")
    private String privilegeAuto;
    @Size(max = 6)
    @Column(name = "privilege_askid")
    private String privilegeAskid;
    @Size(max = 5)
    @Column(name = "privilege_walking")
    private String privilegeWalking;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "privilege_aertube")
    private String privilegeAertube;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "privilege_holiday")
    private String privilegeHoliday;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "privilege_exams_base")
    private String privilegeExamsBase;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 9)
    @Column(name = "privilege_vacation_shedule")
    private String privilegeVacationShedule;
    @Column(name = "id_departament")
    private Short idDepartament;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 23)
    @Column(name = "medcontrol_value")
    private String medcontrolValue;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "category_unit")
    private String categoryUnit;
    @Size(max = 40)
    @Column(name = "num_certificate")
    private String numCertificate;
    @Column(name = "medical_view")
    @Temporal(TemporalType.DATE)
    private Date medicalView;

    public DbDataAll() {
    }

    public DbDataAll(Short personNumber) {
        this.personNumber = personNumber;
    }

    public DbDataAll(Short personNumber, String initials, String surname, String name, String patronymic, String post, String gender, String vCard, String visible, String privilegeAertube, String privilegeHoliday, String privilegeExamsBase, String privilegeVacationShedule, String medcontrolValue, String categoryUnit) {
        this.personNumber = personNumber;
        this.initials = initials;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.post = post;
        this.gender = gender;
        this.vCard = vCard;
        this.visible = visible;
        this.privilegeAertube = privilegeAertube;
        this.privilegeHoliday = privilegeHoliday;
        this.privilegeExamsBase = privilegeExamsBase;
        this.privilegeVacationShedule = privilegeVacationShedule;
        this.medcontrolValue = medcontrolValue;
        this.categoryUnit = categoryUnit;
    }

    public Short getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(Short personNumber) {
        this.personNumber = personNumber;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    public String getDepartamentAbbreviated() {
        return departamentAbbreviated;
    }

    public void setDepartamentAbbreviated(String departamentAbbreviated) {
        this.departamentAbbreviated = departamentAbbreviated;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public void setEmailPassword(String emailPassword) {
        this.emailPassword = emailPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPager() {
        return pager;
    }

    public void setPager(String pager) {
        this.pager = pager;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPCname() {
        return pCname;
    }

    public void setPCname(String pCname) {
        this.pCname = pCname;
    }

    public Short getIPaddr() {
        return iPaddr;
    }

    public void setIPaddr(Short iPaddr) {
        this.iPaddr = iPaddr;
    }

    public String getVCard() {
        return vCard;
    }

    public void setVCard(String vCard) {
        this.vCard = vCard;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getDateInviteWork() {
        return dateInviteWork;
    }

    public void setDateInviteWork(Date dateInviteWork) {
        this.dateInviteWork = dateInviteWork;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getPrivilegeAuto() {
        return privilegeAuto;
    }

    public void setPrivilegeAuto(String privilegeAuto) {
        this.privilegeAuto = privilegeAuto;
    }

    public String getPrivilegeAskid() {
        return privilegeAskid;
    }

    public void setPrivilegeAskid(String privilegeAskid) {
        this.privilegeAskid = privilegeAskid;
    }

    public String getPrivilegeWalking() {
        return privilegeWalking;
    }

    public void setPrivilegeWalking(String privilegeWalking) {
        this.privilegeWalking = privilegeWalking;
    }

    public String getPrivilegeAertube() {
        return privilegeAertube;
    }

    public void setPrivilegeAertube(String privilegeAertube) {
        this.privilegeAertube = privilegeAertube;
    }

    public String getPrivilegeHoliday() {
        return privilegeHoliday;
    }

    public void setPrivilegeHoliday(String privilegeHoliday) {
        this.privilegeHoliday = privilegeHoliday;
    }

    public String getPrivilegeExamsBase() {
        return privilegeExamsBase;
    }

    public void setPrivilegeExamsBase(String privilegeExamsBase) {
        this.privilegeExamsBase = privilegeExamsBase;
    }

    public String getPrivilegeVacationShedule() {
        return privilegeVacationShedule;
    }

    public void setPrivilegeVacationShedule(String privilegeVacationShedule) {
        this.privilegeVacationShedule = privilegeVacationShedule;
    }

    public Short getIdDepartament() {
        return idDepartament;
    }

    public void setIdDepartament(Short idDepartament) {
        this.idDepartament = idDepartament;
    }

    public String getMedcontrolValue() {
        return medcontrolValue;
    }

    public void setMedcontrolValue(String medcontrolValue) {
        this.medcontrolValue = medcontrolValue;
    }

    public String getCategoryUnit() {
        return categoryUnit;
    }

    public void setCategoryUnit(String categoryUnit) {
        this.categoryUnit = categoryUnit;
    }

    public String getNumCertificate() {
        return numCertificate;
    }

    public void setNumCertificate(String numCertificate) {
        this.numCertificate = numCertificate;
    }

    public Date getMedicalView() {
        return medicalView;
    }

    public void setMedicalView(Date medicalView) {
        this.medicalView = medicalView;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personNumber != null ? personNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DbDataAll)) {
            return false;
        }
        DbDataAll other = (DbDataAll) object;
        if ((this.personNumber == null && other.personNumber != null) || (this.personNumber != null && !this.personNumber.equals(other.personNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbDataAll[ personNumber=" + personNumber + " ]";
    }
    
}
