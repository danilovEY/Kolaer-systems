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
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Danilov
 */
@Entity
@Table(name = "db_log_users", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbLogUsers implements Serializable {

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
    @Size(max = 100)
    @Column(name = "departament_abbreviated")
    private String departamentAbbreviated;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String post;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    private String status;

    public DbLogUsers() {
    }

    public DbLogUsers(Short personNumber) {
        this.personNumber = personNumber;
    }

    public DbLogUsers(Short personNumber, String initials, String post, String status) {
        this.personNumber = personNumber;
        this.initials = initials;
        this.post = post;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        if (!(object instanceof DbLogUsers)) {
            return false;
        }
        DbLogUsers other = (DbLogUsers) object;
        if ((this.personNumber == null && other.personNumber != null) || (this.personNumber != null && !this.personNumber.equals(other.personNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbLogUsers[ personNumber=" + personNumber + " ]";
    }
    
}
