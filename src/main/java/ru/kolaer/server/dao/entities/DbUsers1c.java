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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Danilov
 */
//@Entity
//@Table(name = "db_users_1c", catalog = "kolaer_base", schema = "")
//@XmlRootElement
public class DbUsers1c implements Serializable {

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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String departament;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
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
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String vCard;
    @Temporal(TemporalType.DATE)
    private Date birthday;
    @Column(name = "date_invite_work")
    @Temporal(TemporalType.DATE)
    private Date dateInviteWork;

    public DbUsers1c() {
    }

    public DbUsers1c(Short personNumber) {
        this.personNumber = personNumber;
    }

    public DbUsers1c(Short personNumber, String initials, String surname, String name, String patronymic, String departament, String departamentAbbreviated, String post, String gender, String vCard) {
        this.personNumber = personNumber;
        this.initials = initials;
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.departament = departament;
        this.departamentAbbreviated = departamentAbbreviated;
        this.post = post;
        this.gender = gender;
        this.vCard = vCard;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (personNumber != null ? personNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DbUsers1c)) {
            return false;
        }
        DbUsers1c other = (DbUsers1c) object;
        if ((this.personNumber == null && other.personNumber != null) || (this.personNumber != null && !this.personNumber.equals(other.personNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbUsers1c[ personNumber=" + personNumber + " ]";
    }
    
}
