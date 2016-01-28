/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kolaer.server.dao.entities;

import java.util.Date;

public class DbDataAll{
    private Short personNumber;
    private String initials;
    private String surname;
    private String name;
    private String patronymic;
    private String departament;
    private String departamentAbbreviated;
    private String post;
    private String gender;
    private String email;
    private String emailPassword;
    private String phone;
    private String mobilePhone;
    private String pager;
    private String room;
    private String login;
    private String password;
    private String pCname;
    private Short iPaddr;
    private String vCard;
    private Date birthday;
    private Date dateInviteWork;
    private String visible;
    private String privilegeAuto;
    private String privilegeAskid;
    private String privilegeWalking;
    private String privilegeAertube;
    private String privilegeHoliday;
    private String privilegeExamsBase;
    private String privilegeVacationShedule;
    private Short idDepartament;
    private String medcontrolValue;
    private String categoryUnit;
    private String numCertificate;
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
