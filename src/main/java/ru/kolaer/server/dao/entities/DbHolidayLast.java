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
 *
 * @author Danilov
 */
@Entity
@Table(name = "db_holiday_last", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbHolidayLast implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "person_number")
    private Short personNumber;
    @Column(name = "year_holiday")
    @Temporal(TemporalType.DATE)
    private Date yearHoliday;
    @Size(max = 10)
    @Column(name = "tag_holiday_1")
    private String tagHoliday1;
    @Size(max = 13)
    @Column(name = "select_month_1")
    private String selectMonth1;
    @Column(name = "dt_holiday_from_1")
    @Temporal(TemporalType.DATE)
    private Date dtHolidayFrom1;
    @Column(name = "dt_holiday_to_1")
    @Temporal(TemporalType.DATE)
    private Date dtHolidayTo1;
    @Column(name = "busy_day_1")
    private Short busyDay1;
    @Size(max = 6)
    @Column(name = "color_1")
    private String color1;
    @Size(max = 10)
    @Column(name = "tag_holiday_2")
    private String tagHoliday2;
    @Size(max = 13)
    @Column(name = "select_month_2")
    private String selectMonth2;
    @Column(name = "dt_holiday_from_2")
    @Temporal(TemporalType.DATE)
    private Date dtHolidayFrom2;
    @Column(name = "dt_holiday_to_2")
    @Temporal(TemporalType.DATE)
    private Date dtHolidayTo2;
    @Column(name = "busy_day_2")
    private Short busyDay2;
    @Size(max = 6)
    @Column(name = "color_2")
    private String color2;
    @Size(max = 10)
    @Column(name = "tag_holiday_3")
    private String tagHoliday3;
    @Size(max = 13)
    @Column(name = "select_month_3")
    private String selectMonth3;
    @Column(name = "dt_holiday_from_3")
    @Temporal(TemporalType.DATE)
    private Date dtHolidayFrom3;
    @Column(name = "dt_holiday_to_3")
    @Temporal(TemporalType.DATE)
    private Date dtHolidayTo3;
    @Column(name = "busy_day_3")
    private Short busyDay3;
    @Size(max = 6)
    @Column(name = "color_3")
    private String color3;
    @Size(max = 10)
    @Column(name = "tag_holiday_4")
    private String tagHoliday4;
    @Size(max = 13)
    @Column(name = "select_month_4")
    private String selectMonth4;
    @Column(name = "dt_holiday_from_4")
    @Temporal(TemporalType.DATE)
    private Date dtHolidayFrom4;
    @Column(name = "dt_holiday_to_4")
    @Temporal(TemporalType.DATE)
    private Date dtHolidayTo4;
    @Column(name = "busy_day_4")
    private Short busyDay4;
    @Size(max = 6)
    @Column(name = "color_4")
    private String color4;
    @Size(max = 10)
    @Column(name = "tag_holiday_5")
    private String tagHoliday5;
    @Size(max = 13)
    @Column(name = "select_month_5")
    private String selectMonth5;
    @Column(name = "dt_holiday_from_5")
    @Temporal(TemporalType.DATE)
    private Date dtHolidayFrom5;
    @Column(name = "dt_holiday_to_5")
    @Temporal(TemporalType.DATE)
    private Date dtHolidayTo5;
    @Column(name = "busy_day_5")
    private Short busyDay5;
    @Size(max = 6)
    @Column(name = "color_5")
    private String color5;

    public DbHolidayLast() {
    }

    public DbHolidayLast(Short personNumber) {
        this.personNumber = personNumber;
    }

    public Short getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(Short personNumber) {
        this.personNumber = personNumber;
    }

    public Date getYearHoliday() {
        return yearHoliday;
    }

    public void setYearHoliday(Date yearHoliday) {
        this.yearHoliday = yearHoliday;
    }

    public String getTagHoliday1() {
        return tagHoliday1;
    }

    public void setTagHoliday1(String tagHoliday1) {
        this.tagHoliday1 = tagHoliday1;
    }

    public String getSelectMonth1() {
        return selectMonth1;
    }

    public void setSelectMonth1(String selectMonth1) {
        this.selectMonth1 = selectMonth1;
    }

    public Date getDtHolidayFrom1() {
        return dtHolidayFrom1;
    }

    public void setDtHolidayFrom1(Date dtHolidayFrom1) {
        this.dtHolidayFrom1 = dtHolidayFrom1;
    }

    public Date getDtHolidayTo1() {
        return dtHolidayTo1;
    }

    public void setDtHolidayTo1(Date dtHolidayTo1) {
        this.dtHolidayTo1 = dtHolidayTo1;
    }

    public Short getBusyDay1() {
        return busyDay1;
    }

    public void setBusyDay1(Short busyDay1) {
        this.busyDay1 = busyDay1;
    }

    public String getColor1() {
        return color1;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public String getTagHoliday2() {
        return tagHoliday2;
    }

    public void setTagHoliday2(String tagHoliday2) {
        this.tagHoliday2 = tagHoliday2;
    }

    public String getSelectMonth2() {
        return selectMonth2;
    }

    public void setSelectMonth2(String selectMonth2) {
        this.selectMonth2 = selectMonth2;
    }

    public Date getDtHolidayFrom2() {
        return dtHolidayFrom2;
    }

    public void setDtHolidayFrom2(Date dtHolidayFrom2) {
        this.dtHolidayFrom2 = dtHolidayFrom2;
    }

    public Date getDtHolidayTo2() {
        return dtHolidayTo2;
    }

    public void setDtHolidayTo2(Date dtHolidayTo2) {
        this.dtHolidayTo2 = dtHolidayTo2;
    }

    public Short getBusyDay2() {
        return busyDay2;
    }

    public void setBusyDay2(Short busyDay2) {
        this.busyDay2 = busyDay2;
    }

    public String getColor2() {
        return color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public String getTagHoliday3() {
        return tagHoliday3;
    }

    public void setTagHoliday3(String tagHoliday3) {
        this.tagHoliday3 = tagHoliday3;
    }

    public String getSelectMonth3() {
        return selectMonth3;
    }

    public void setSelectMonth3(String selectMonth3) {
        this.selectMonth3 = selectMonth3;
    }

    public Date getDtHolidayFrom3() {
        return dtHolidayFrom3;
    }

    public void setDtHolidayFrom3(Date dtHolidayFrom3) {
        this.dtHolidayFrom3 = dtHolidayFrom3;
    }

    public Date getDtHolidayTo3() {
        return dtHolidayTo3;
    }

    public void setDtHolidayTo3(Date dtHolidayTo3) {
        this.dtHolidayTo3 = dtHolidayTo3;
    }

    public Short getBusyDay3() {
        return busyDay3;
    }

    public void setBusyDay3(Short busyDay3) {
        this.busyDay3 = busyDay3;
    }

    public String getColor3() {
        return color3;
    }

    public void setColor3(String color3) {
        this.color3 = color3;
    }

    public String getTagHoliday4() {
        return tagHoliday4;
    }

    public void setTagHoliday4(String tagHoliday4) {
        this.tagHoliday4 = tagHoliday4;
    }

    public String getSelectMonth4() {
        return selectMonth4;
    }

    public void setSelectMonth4(String selectMonth4) {
        this.selectMonth4 = selectMonth4;
    }

    public Date getDtHolidayFrom4() {
        return dtHolidayFrom4;
    }

    public void setDtHolidayFrom4(Date dtHolidayFrom4) {
        this.dtHolidayFrom4 = dtHolidayFrom4;
    }

    public Date getDtHolidayTo4() {
        return dtHolidayTo4;
    }

    public void setDtHolidayTo4(Date dtHolidayTo4) {
        this.dtHolidayTo4 = dtHolidayTo4;
    }

    public Short getBusyDay4() {
        return busyDay4;
    }

    public void setBusyDay4(Short busyDay4) {
        this.busyDay4 = busyDay4;
    }

    public String getColor4() {
        return color4;
    }

    public void setColor4(String color4) {
        this.color4 = color4;
    }

    public String getTagHoliday5() {
        return tagHoliday5;
    }

    public void setTagHoliday5(String tagHoliday5) {
        this.tagHoliday5 = tagHoliday5;
    }

    public String getSelectMonth5() {
        return selectMonth5;
    }

    public void setSelectMonth5(String selectMonth5) {
        this.selectMonth5 = selectMonth5;
    }

    public Date getDtHolidayFrom5() {
        return dtHolidayFrom5;
    }

    public void setDtHolidayFrom5(Date dtHolidayFrom5) {
        this.dtHolidayFrom5 = dtHolidayFrom5;
    }

    public Date getDtHolidayTo5() {
        return dtHolidayTo5;
    }

    public void setDtHolidayTo5(Date dtHolidayTo5) {
        this.dtHolidayTo5 = dtHolidayTo5;
    }

    public Short getBusyDay5() {
        return busyDay5;
    }

    public void setBusyDay5(Short busyDay5) {
        this.busyDay5 = busyDay5;
    }

    public String getColor5() {
        return color5;
    }

    public void setColor5(String color5) {
        this.color5 = color5;
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
        if (!(object instanceof DbHolidayLast)) {
            return false;
        }
        DbHolidayLast other = (DbHolidayLast) object;
        if ((this.personNumber == null && other.personNumber != null) || (this.personNumber != null && !this.personNumber.equals(other.personNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbHolidayLast[ personNumber=" + personNumber + " ]";
    }
    
}
