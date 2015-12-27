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
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Danilov
 */
@Entity
@Table(name = "db_holiday_freeday_last", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbHolidayFreedayLast implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "person_number")
    private Short personNumber;
    @Temporal(TemporalType.DATE)
    private Date year;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    private BigDecimal factor;
    private Short day;
    private Short balance;
    @Column(name = "factor_january")
    private BigDecimal factorJanuary;
    private Float january;
    @Column(name = "january_middle")
    private Integer januaryMiddle;
    @Column(name = "january_end")
    private Float januaryEnd;
    @Column(name = "factor_february")
    private BigDecimal factorFebruary;
    private Float february;
    @Column(name = "february_middle")
    private Integer februaryMiddle;
    @Column(name = "february_end")
    private Float februaryEnd;
    @Column(name = "factor_march")
    private BigDecimal factorMarch;
    private Float march;
    @Column(name = "march_middle")
    private Integer marchMiddle;
    @Column(name = "march_end")
    private Float marchEnd;
    @Column(name = "factor_april")
    private BigDecimal factorApril;
    private Float april;
    @Column(name = "april_middle")
    private Integer aprilMiddle;
    @Column(name = "april_end")
    private Float aprilEnd;
    @Column(name = "factor_may")
    private BigDecimal factorMay;
    private Float may;
    @Column(name = "may_middle")
    private Integer mayMiddle;
    @Column(name = "may_end")
    private Float mayEnd;
    @Column(name = "factor_june")
    private BigDecimal factorJune;
    private Float june;
    @Column(name = "june_middle")
    private Integer juneMiddle;
    @Column(name = "june_end")
    private Float juneEnd;
    @Column(name = "factor_july")
    private BigDecimal factorJuly;
    private Float july;
    @Column(name = "july_middle")
    private Integer julyMiddle;
    @Column(name = "july_end")
    private Float julyEnd;
    @Column(name = "factor_august")
    private BigDecimal factorAugust;
    private Float august;
    @Column(name = "august_middle")
    private Integer augustMiddle;
    @Column(name = "august_end")
    private Float augustEnd;
    @Column(name = "factor_september")
    private BigDecimal factorSeptember;
    private Float september;
    @Column(name = "september_middle")
    private Integer septemberMiddle;
    @Column(name = "september_end")
    private Float septemberEnd;
    @Column(name = "factor_october")
    private BigDecimal factorOctober;
    private Float october;
    @Column(name = "october_middle")
    private Integer octoberMiddle;
    @Column(name = "october_end")
    private Float octoberEnd;
    @Column(name = "factor_november")
    private BigDecimal factorNovember;
    private Float november;
    @Column(name = "november_middle")
    private Integer novemberMiddle;
    @Column(name = "november_end")
    private Float novemberEnd;
    @Column(name = "factor_december")
    private BigDecimal factorDecember;
    private Float december;
    @Column(name = "december_middle")
    private Integer decemberMiddle;
    @Column(name = "december_end")
    private Float decemberEnd;

    public DbHolidayFreedayLast() {
    }

    public DbHolidayFreedayLast(Short personNumber) {
        this.personNumber = personNumber;
    }

    public Short getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(Short personNumber) {
        this.personNumber = personNumber;
    }

    public Date getYear() {
        return year;
    }

    public void setYear(Date year) {
        this.year = year;
    }

    public BigDecimal getFactor() {
        return factor;
    }

    public void setFactor(BigDecimal factor) {
        this.factor = factor;
    }

    public Short getDay() {
        return day;
    }

    public void setDay(Short day) {
        this.day = day;
    }

    public Short getBalance() {
        return balance;
    }

    public void setBalance(Short balance) {
        this.balance = balance;
    }

    public BigDecimal getFactorJanuary() {
        return factorJanuary;
    }

    public void setFactorJanuary(BigDecimal factorJanuary) {
        this.factorJanuary = factorJanuary;
    }

    public Float getJanuary() {
        return january;
    }

    public void setJanuary(Float january) {
        this.january = january;
    }

    public Integer getJanuaryMiddle() {
        return januaryMiddle;
    }

    public void setJanuaryMiddle(Integer januaryMiddle) {
        this.januaryMiddle = januaryMiddle;
    }

    public Float getJanuaryEnd() {
        return januaryEnd;
    }

    public void setJanuaryEnd(Float januaryEnd) {
        this.januaryEnd = januaryEnd;
    }

    public BigDecimal getFactorFebruary() {
        return factorFebruary;
    }

    public void setFactorFebruary(BigDecimal factorFebruary) {
        this.factorFebruary = factorFebruary;
    }

    public Float getFebruary() {
        return february;
    }

    public void setFebruary(Float february) {
        this.february = february;
    }

    public Integer getFebruaryMiddle() {
        return februaryMiddle;
    }

    public void setFebruaryMiddle(Integer februaryMiddle) {
        this.februaryMiddle = februaryMiddle;
    }

    public Float getFebruaryEnd() {
        return februaryEnd;
    }

    public void setFebruaryEnd(Float februaryEnd) {
        this.februaryEnd = februaryEnd;
    }

    public BigDecimal getFactorMarch() {
        return factorMarch;
    }

    public void setFactorMarch(BigDecimal factorMarch) {
        this.factorMarch = factorMarch;
    }

    public Float getMarch() {
        return march;
    }

    public void setMarch(Float march) {
        this.march = march;
    }

    public Integer getMarchMiddle() {
        return marchMiddle;
    }

    public void setMarchMiddle(Integer marchMiddle) {
        this.marchMiddle = marchMiddle;
    }

    public Float getMarchEnd() {
        return marchEnd;
    }

    public void setMarchEnd(Float marchEnd) {
        this.marchEnd = marchEnd;
    }

    public BigDecimal getFactorApril() {
        return factorApril;
    }

    public void setFactorApril(BigDecimal factorApril) {
        this.factorApril = factorApril;
    }

    public Float getApril() {
        return april;
    }

    public void setApril(Float april) {
        this.april = april;
    }

    public Integer getAprilMiddle() {
        return aprilMiddle;
    }

    public void setAprilMiddle(Integer aprilMiddle) {
        this.aprilMiddle = aprilMiddle;
    }

    public Float getAprilEnd() {
        return aprilEnd;
    }

    public void setAprilEnd(Float aprilEnd) {
        this.aprilEnd = aprilEnd;
    }

    public BigDecimal getFactorMay() {
        return factorMay;
    }

    public void setFactorMay(BigDecimal factorMay) {
        this.factorMay = factorMay;
    }

    public Float getMay() {
        return may;
    }

    public void setMay(Float may) {
        this.may = may;
    }

    public Integer getMayMiddle() {
        return mayMiddle;
    }

    public void setMayMiddle(Integer mayMiddle) {
        this.mayMiddle = mayMiddle;
    }

    public Float getMayEnd() {
        return mayEnd;
    }

    public void setMayEnd(Float mayEnd) {
        this.mayEnd = mayEnd;
    }

    public BigDecimal getFactorJune() {
        return factorJune;
    }

    public void setFactorJune(BigDecimal factorJune) {
        this.factorJune = factorJune;
    }

    public Float getJune() {
        return june;
    }

    public void setJune(Float june) {
        this.june = june;
    }

    public Integer getJuneMiddle() {
        return juneMiddle;
    }

    public void setJuneMiddle(Integer juneMiddle) {
        this.juneMiddle = juneMiddle;
    }

    public Float getJuneEnd() {
        return juneEnd;
    }

    public void setJuneEnd(Float juneEnd) {
        this.juneEnd = juneEnd;
    }

    public BigDecimal getFactorJuly() {
        return factorJuly;
    }

    public void setFactorJuly(BigDecimal factorJuly) {
        this.factorJuly = factorJuly;
    }

    public Float getJuly() {
        return july;
    }

    public void setJuly(Float july) {
        this.july = july;
    }

    public Integer getJulyMiddle() {
        return julyMiddle;
    }

    public void setJulyMiddle(Integer julyMiddle) {
        this.julyMiddle = julyMiddle;
    }

    public Float getJulyEnd() {
        return julyEnd;
    }

    public void setJulyEnd(Float julyEnd) {
        this.julyEnd = julyEnd;
    }

    public BigDecimal getFactorAugust() {
        return factorAugust;
    }

    public void setFactorAugust(BigDecimal factorAugust) {
        this.factorAugust = factorAugust;
    }

    public Float getAugust() {
        return august;
    }

    public void setAugust(Float august) {
        this.august = august;
    }

    public Integer getAugustMiddle() {
        return augustMiddle;
    }

    public void setAugustMiddle(Integer augustMiddle) {
        this.augustMiddle = augustMiddle;
    }

    public Float getAugustEnd() {
        return augustEnd;
    }

    public void setAugustEnd(Float augustEnd) {
        this.augustEnd = augustEnd;
    }

    public BigDecimal getFactorSeptember() {
        return factorSeptember;
    }

    public void setFactorSeptember(BigDecimal factorSeptember) {
        this.factorSeptember = factorSeptember;
    }

    public Float getSeptember() {
        return september;
    }

    public void setSeptember(Float september) {
        this.september = september;
    }

    public Integer getSeptemberMiddle() {
        return septemberMiddle;
    }

    public void setSeptemberMiddle(Integer septemberMiddle) {
        this.septemberMiddle = septemberMiddle;
    }

    public Float getSeptemberEnd() {
        return septemberEnd;
    }

    public void setSeptemberEnd(Float septemberEnd) {
        this.septemberEnd = septemberEnd;
    }

    public BigDecimal getFactorOctober() {
        return factorOctober;
    }

    public void setFactorOctober(BigDecimal factorOctober) {
        this.factorOctober = factorOctober;
    }

    public Float getOctober() {
        return october;
    }

    public void setOctober(Float october) {
        this.october = october;
    }

    public Integer getOctoberMiddle() {
        return octoberMiddle;
    }

    public void setOctoberMiddle(Integer octoberMiddle) {
        this.octoberMiddle = octoberMiddle;
    }

    public Float getOctoberEnd() {
        return octoberEnd;
    }

    public void setOctoberEnd(Float octoberEnd) {
        this.octoberEnd = octoberEnd;
    }

    public BigDecimal getFactorNovember() {
        return factorNovember;
    }

    public void setFactorNovember(BigDecimal factorNovember) {
        this.factorNovember = factorNovember;
    }

    public Float getNovember() {
        return november;
    }

    public void setNovember(Float november) {
        this.november = november;
    }

    public Integer getNovemberMiddle() {
        return novemberMiddle;
    }

    public void setNovemberMiddle(Integer novemberMiddle) {
        this.novemberMiddle = novemberMiddle;
    }

    public Float getNovemberEnd() {
        return novemberEnd;
    }

    public void setNovemberEnd(Float novemberEnd) {
        this.novemberEnd = novemberEnd;
    }

    public BigDecimal getFactorDecember() {
        return factorDecember;
    }

    public void setFactorDecember(BigDecimal factorDecember) {
        this.factorDecember = factorDecember;
    }

    public Float getDecember() {
        return december;
    }

    public void setDecember(Float december) {
        this.december = december;
    }

    public Integer getDecemberMiddle() {
        return decemberMiddle;
    }

    public void setDecemberMiddle(Integer decemberMiddle) {
        this.decemberMiddle = decemberMiddle;
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
        hash += (personNumber != null ? personNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DbHolidayFreedayLast)) {
            return false;
        }
        DbHolidayFreedayLast other = (DbHolidayFreedayLast) object;
        if ((this.personNumber == null && other.personNumber != null) || (this.personNumber != null && !this.personNumber.equals(other.personNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbHolidayFreedayLast[ personNumber=" + personNumber + " ]";
    }
    
}
