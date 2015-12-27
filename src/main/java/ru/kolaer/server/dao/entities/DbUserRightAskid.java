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
@Table(name = "db_user_right_askid", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbUserRightAskid implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "person_number")
    private Short personNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    private String access;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "rule_0")
    private String rule0;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "rule_1")
    private String rule1;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "rule_2")
    private String rule2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "rule_3")
    private String rule3;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "rule_4")
    private String rule4;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "rule_5")
    private String rule5;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 3)
    @Column(name = "rule_6")
    private String rule6;

    public DbUserRightAskid() {
    }

    public DbUserRightAskid(Short personNumber) {
        this.personNumber = personNumber;
    }

    public DbUserRightAskid(Short personNumber, String access, String rule0, String rule1, String rule2, String rule3, String rule4, String rule5, String rule6) {
        this.personNumber = personNumber;
        this.access = access;
        this.rule0 = rule0;
        this.rule1 = rule1;
        this.rule2 = rule2;
        this.rule3 = rule3;
        this.rule4 = rule4;
        this.rule5 = rule5;
        this.rule6 = rule6;
    }

    public Short getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(Short personNumber) {
        this.personNumber = personNumber;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getRule0() {
        return rule0;
    }

    public void setRule0(String rule0) {
        this.rule0 = rule0;
    }

    public String getRule1() {
        return rule1;
    }

    public void setRule1(String rule1) {
        this.rule1 = rule1;
    }

    public String getRule2() {
        return rule2;
    }

    public void setRule2(String rule2) {
        this.rule2 = rule2;
    }

    public String getRule3() {
        return rule3;
    }

    public void setRule3(String rule3) {
        this.rule3 = rule3;
    }

    public String getRule4() {
        return rule4;
    }

    public void setRule4(String rule4) {
        this.rule4 = rule4;
    }

    public String getRule5() {
        return rule5;
    }

    public void setRule5(String rule5) {
        this.rule5 = rule5;
    }

    public String getRule6() {
        return rule6;
    }

    public void setRule6(String rule6) {
        this.rule6 = rule6;
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
        if (!(object instanceof DbUserRightAskid)) {
            return false;
        }
        DbUserRightAskid other = (DbUserRightAskid) object;
        if ((this.personNumber == null && other.personNumber != null) || (this.personNumber != null && !this.personNumber.equals(other.personNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbUserRightAskid[ personNumber=" + personNumber + " ]";
    }
    
}
