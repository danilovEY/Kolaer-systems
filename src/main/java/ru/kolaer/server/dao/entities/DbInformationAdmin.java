/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kolaer.server.dao.entities;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Danilov
 */
@Entity
@Table(name = "db_information_admin", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbInformationAdmin implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Short id;
    @Column(name = "person_number")
    private Short personNumber;
    @Size(max = 50)
    private String surname;
    @Size(max = 50)
    private String name;
    @Size(max = 50)
    private String patronymic;
    @Size(max = 100)
    @Column(name = "departament_abbreviated")
    private String departamentAbbreviated;
    @Size(max = 3)
    @Column(name = "param_SAPSRM")
    private String paramSAPSRM;
    @Size(max = 100)
    @Column(name = "SAPSRM_login")
    private String sAPSRMlogin;
    @Size(max = 100)
    @Column(name = "SAPSRM_password")
    private String sAPSRMpassword;
    @Size(max = 3)
    @Column(name = "param_NOVELL")
    private String paramNOVELL;
    @Size(max = 100)
    @Column(name = "NOVELL_login")
    private String nOVELLlogin;
    @Size(max = 100)
    @Column(name = "NOVELL_password")
    private String nOVELLpassword;

    public DbInformationAdmin() {
    }

    public DbInformationAdmin(Short id) {
        this.id = id;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public Short getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(Short personNumber) {
        this.personNumber = personNumber;
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

    public String getDepartamentAbbreviated() {
        return departamentAbbreviated;
    }

    public void setDepartamentAbbreviated(String departamentAbbreviated) {
        this.departamentAbbreviated = departamentAbbreviated;
    }

    public String getParamSAPSRM() {
        return paramSAPSRM;
    }

    public void setParamSAPSRM(String paramSAPSRM) {
        this.paramSAPSRM = paramSAPSRM;
    }

    public String getSAPSRMlogin() {
        return sAPSRMlogin;
    }

    public void setSAPSRMlogin(String sAPSRMlogin) {
        this.sAPSRMlogin = sAPSRMlogin;
    }

    public String getSAPSRMpassword() {
        return sAPSRMpassword;
    }

    public void setSAPSRMpassword(String sAPSRMpassword) {
        this.sAPSRMpassword = sAPSRMpassword;
    }

    public String getParamNOVELL() {
        return paramNOVELL;
    }

    public void setParamNOVELL(String paramNOVELL) {
        this.paramNOVELL = paramNOVELL;
    }

    public String getNOVELLlogin() {
        return nOVELLlogin;
    }

    public void setNOVELLlogin(String nOVELLlogin) {
        this.nOVELLlogin = nOVELLlogin;
    }

    public String getNOVELLpassword() {
        return nOVELLpassword;
    }

    public void setNOVELLpassword(String nOVELLpassword) {
        this.nOVELLpassword = nOVELLpassword;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DbInformationAdmin)) {
            return false;
        }
        DbInformationAdmin other = (DbInformationAdmin) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbInformationAdmin[ id=" + id + " ]";
    }
    
}
