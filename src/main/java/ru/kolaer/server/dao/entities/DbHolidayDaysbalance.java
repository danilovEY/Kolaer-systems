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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Danilov
 */
@Entity
@Table(name = "db_holiday_daysbalance", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbHolidayDaysbalance implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "person_number")
    private Short personNumber;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private Float january;
    private Float february;
    private Float march;
    private Float april;
    private Float may;
    private Float june;
    private Float july;
    private Float august;
    private Float september;
    private Float october;
    private Float november;
    private Float december;

    public DbHolidayDaysbalance() {
    }

    public DbHolidayDaysbalance(Short personNumber) {
        this.personNumber = personNumber;
    }

    public Short getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(Short personNumber) {
        this.personNumber = personNumber;
    }

    public Float getJanuary() {
        return january;
    }

    public void setJanuary(Float january) {
        this.january = january;
    }

    public Float getFebruary() {
        return february;
    }

    public void setFebruary(Float february) {
        this.february = february;
    }

    public Float getMarch() {
        return march;
    }

    public void setMarch(Float march) {
        this.march = march;
    }

    public Float getApril() {
        return april;
    }

    public void setApril(Float april) {
        this.april = april;
    }

    public Float getMay() {
        return may;
    }

    public void setMay(Float may) {
        this.may = may;
    }

    public Float getJune() {
        return june;
    }

    public void setJune(Float june) {
        this.june = june;
    }

    public Float getJuly() {
        return july;
    }

    public void setJuly(Float july) {
        this.july = july;
    }

    public Float getAugust() {
        return august;
    }

    public void setAugust(Float august) {
        this.august = august;
    }

    public Float getSeptember() {
        return september;
    }

    public void setSeptember(Float september) {
        this.september = september;
    }

    public Float getOctober() {
        return october;
    }

    public void setOctober(Float october) {
        this.october = october;
    }

    public Float getNovember() {
        return november;
    }

    public void setNovember(Float november) {
        this.november = november;
    }

    public Float getDecember() {
        return december;
    }

    public void setDecember(Float december) {
        this.december = december;
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
        if (!(object instanceof DbHolidayDaysbalance)) {
            return false;
        }
        DbHolidayDaysbalance other = (DbHolidayDaysbalance) object;
        if ((this.personNumber == null && other.personNumber != null) || (this.personNumber != null && !this.personNumber.equals(other.personNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbHolidayDaysbalance[ personNumber=" + personNumber + " ]";
    }
    
}
