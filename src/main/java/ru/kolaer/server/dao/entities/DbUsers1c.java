/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kolaer.server.dao.entities;

import java.util.Date;

public class DbUsers1c {
    private Short personNumber;
    private String initials;
    private String surname;
    private String name;
    private String patronymic;
    private String departament;
    private String departamentAbbreviated;
    private String post;
    private String gender;
    private String vCard;
    private Date birthday;
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
