/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kolaer.server.dao.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Danilov
 */
@MappedSuperclass
@Table(name = "db_vacation_shedule", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbVacationShedule implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Short id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "person_number")
    private short personNumber;
    @Temporal(TemporalType.DATE)
    private Date year;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "january_factor")
    private BigDecimal januaryFactor;
    @Column(name = "january_start")
    private Float januaryStart;
    @Column(name = "january_end")
    private Float januaryEnd;
    @Column(name = "february_factor")
    private BigDecimal februaryFactor;
    @Column(name = "february_start")
    private Float februaryStart;
    @Column(name = "february_end")
    private Float februaryEnd;
    @Column(name = "march_factor")
    private BigDecimal marchFactor;
    @Column(name = "march_start")
    private Float marchStart;
    @Column(name = "march_end")
    private Float marchEnd;
    @Column(name = "april_factor")
    private BigDecimal aprilFactor;
    @Column(name = "april_start")
    private Float aprilStart;
    @Column(name = "april_end")
    private Float aprilEnd;
    @Column(name = "may_factor")
    private BigDecimal mayFactor;
    @Column(name = "may_start")
    private Float mayStart;
    @Column(name = "may_end")
    private Float mayEnd;
    @Column(name = "june_factor")
    private BigDecimal juneFactor;
    @Column(name = "june_start")
    private Float juneStart;
    @Column(name = "june_end")
    private Float juneEnd;
    @Column(name = "july_factor")
    private BigDecimal julyFactor;
    @Column(name = "july_start")
    private Float julyStart;
    @Column(name = "july_end")
    private Float julyEnd;
    @Column(name = "august_factor")
    private BigDecimal augustFactor;
    @Column(name = "august_start")
    private Float augustStart;
    @Column(name = "august_end")
    private Float augustEnd;
    @Column(name = "september_factor")
    private BigDecimal septemberFactor;
    @Column(name = "september_start")
    private Float septemberStart;
    @Column(name = "september_end")
    private Float septemberEnd;
    @Column(name = "october_factor")
    private BigDecimal octoberFactor;
    @Column(name = "october_start")
    private Float octoberStart;
    @Column(name = "october_end")
    private Float octoberEnd;
    @Column(name = "november_factor")
    private BigDecimal novemberFactor;
    @Column(name = "november_start")
    private Float novemberStart;
    @Column(name = "november_end")
    private Float novemberEnd;
    @Column(name = "december_factor")
    private BigDecimal decemberFactor;
    @Column(name = "december_start")
    private Float decemberStart;
    @Column(name = "december_end")
    private Float decemberEnd;

    public DbVacationShedule() {
    }

    public DbVacationShedule(Short id) {
        this.id = id;
    }

    public DbVacationShedule(Short id, short personNumber) {
        this.id = id;
        this.personNumber = personNumber;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public short getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(short personNumber) {
        this.personNumber = personNumber;
    }

    public Date getYear() {
        return year;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public BigDecimal getJanuaryFactor() {
        return januaryFactor;
    }

    public void setJanuaryFactor(BigDecimal januaryFactor) {
        this.januaryFactor = januaryFactor;
    }

    public Float getJanuaryStart() {
        return januaryStart;
    }

    public void setJanuaryStart(Float januaryStart) {
        this.januaryStart = januaryStart;
    }

    public Float getJanuaryEnd() {
        return januaryEnd;
    }

    public void setJanuaryEnd(Float januaryEnd) {
        this.januaryEnd = januaryEnd;
    }

    public BigDecimal getFebruaryFactor() {
        return februaryFactor;
    }

    public void setFebruaryFactor(BigDecimal februaryFactor) {
        this.februaryFactor = februaryFactor;
    }

    public Float getFebruaryStart() {
        return februaryStart;
    }

    public void setFebruaryStart(Float februaryStart) {
        this.februaryStart = februaryStart;
    }

    public Float getFebruaryEnd() {
        return februaryEnd;
    }

    public void setFebruaryEnd(Float februaryEnd) {
        this.februaryEnd = februaryEnd;
    }

    public BigDecimal getMarchFactor() {
        return marchFactor;
    }

    public void setMarchFactor(BigDecimal marchFactor) {
        this.marchFactor = marchFactor;
    }

    public Float getMarchStart() {
        return marchStart;
    }

    public void setMarchStart(Float marchStart) {
        this.marchStart = marchStart;
    }

    public Float getMarchEnd() {
        return marchEnd;
    }

    public void setMarchEnd(Float marchEnd) {
        this.marchEnd = marchEnd;
    }

    public BigDecimal getAprilFactor() {
        return aprilFactor;
    }

    public void setAprilFactor(BigDecimal aprilFactor) {
        this.aprilFactor = aprilFactor;
    }

    public Float getAprilStart() {
        return aprilStart;
    }

    public void setAprilStart(Float aprilStart) {
        this.aprilStart = aprilStart;
    }

    public Float getAprilEnd() {
        return aprilEnd;
    }

    public void setAprilEnd(Float aprilEnd) {
        this.aprilEnd = aprilEnd;
    }

    public BigDecimal getMayFactor() {
        return mayFactor;
    }

    public void setMayFactor(BigDecimal mayFactor) {
        this.mayFactor = mayFactor;
    }

    public Float getMayStart() {
        return mayStart;
    }

    public void setMayStart(Float mayStart) {
        this.mayStart = mayStart;
    }

    public Float getMayEnd() {
        return mayEnd;
    }

    public void setMayEnd(Float mayEnd) {
        this.mayEnd = mayEnd;
    }

    public BigDecimal getJuneFactor() {
        return juneFactor;
    }

    public void setJuneFactor(BigDecimal juneFactor) {
        this.juneFactor = juneFactor;
    }

    public Float getJuneStart() {
        return juneStart;
    }

    public void setJuneStart(Float juneStart) {
        this.juneStart = juneStart;
    }

    public Float getJuneEnd() {
        return juneEnd;
    }

    public void setJuneEnd(Float juneEnd) {
        this.juneEnd = juneEnd;
    }

    public BigDecimal getJulyFactor() {
        return julyFactor;
    }

    public void setJulyFactor(BigDecimal julyFactor) {
        this.julyFactor = julyFactor;
    }

    public Float getJulyStart() {
        return julyStart;
    }

    public void setJulyStart(Float julyStart) {
        this.julyStart = julyStart;
    }

    public Float getJulyEnd() {
        return julyEnd;
    }

    public void setJulyEnd(Float julyEnd) {
        this.julyEnd = julyEnd;
    }

    public BigDecimal getAugustFactor() {
        return augustFactor;
    }

    public void setAugustFactor(BigDecimal augustFactor) {
        this.augustFactor = augustFactor;
    }

    public Float getAugustStart() {
        return augustStart;
    }

    public void setAugustStart(Float augustStart) {
        this.augustStart = augustStart;
    }

    public Float getAugustEnd() {
        return augustEnd;
    }

    public void setAugustEnd(Float augustEnd) {
        this.augustEnd = augustEnd;
    }

    public BigDecimal getSeptemberFactor() {
        return septemberFactor;
    }

    public void setSeptemberFactor(BigDecimal septemberFactor) {
        this.septemberFactor = septemberFactor;
    }

    public Float getSeptemberStart() {
        return septemberStart;
    }

    public void setSeptemberStart(Float septemberStart) {
        this.septemberStart = septemberStart;
    }

    public Float getSeptemberEnd() {
        return septemberEnd;
    }

    public void setSeptemberEnd(Float septemberEnd) {
        this.septemberEnd = septemberEnd;
    }

    public BigDecimal getOctoberFactor() {
        return octoberFactor;
    }

    public void setOctoberFactor(BigDecimal octoberFactor) {
        this.octoberFactor = octoberFactor;
    }

    public Float getOctoberStart() {
        return octoberStart;
    }

    public void setOctoberStart(Float octoberStart) {
        this.octoberStart = octoberStart;
    }

    public Float getOctoberEnd() {
        return octoberEnd;
    }

    public void setOctoberEnd(Float octoberEnd) {
        this.octoberEnd = octoberEnd;
    }

    public BigDecimal getNovemberFactor() {
        return novemberFactor;
    }

    public void setNovemberFactor(BigDecimal novemberFactor) {
        this.novemberFactor = novemberFactor;
    }

    public Float getNovemberStart() {
        return novemberStart;
    }

    public void setNovemberStart(Float novemberStart) {
        this.novemberStart = novemberStart;
    }

    public Float getNovemberEnd() {
        return novemberEnd;
    }

    public void setNovemberEnd(Float novemberEnd) {
        this.novemberEnd = novemberEnd;
    }

    public BigDecimal getDecemberFactor() {
        return decemberFactor;
    }

    public void setDecemberFactor(BigDecimal decemberFactor) {
        this.decemberFactor = decemberFactor;
    }

    public Float getDecemberStart() {
        return decemberStart;
    }

    public void setDecemberStart(Float decemberStart) {
        this.decemberStart = decemberStart;
    }

    public Float getDecemberEnd() {
        return decemberEnd;
    }

    public void setDecemberEnd(Float decemberEnd) {
        this.decemberEnd = decemberEnd;
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
        if (!(object instanceof DbVacationShedule)) {
            return false;
        }
        DbVacationShedule other = (DbVacationShedule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbVacationShedule[ id=" + id + " ]";
    }
    
}
