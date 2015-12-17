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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
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
@MappedSuperclass
@Table(name = "db_holiday_report1_last", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbHolidayReport1Last implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "departament_abbreviated")
    private String departamentAbbreviated;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    private String post;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    private String initials;
    @Basic(optional = false)
    @NotNull
    @Column(name = "person_number")
    private short personNumber;
    @Basic(optional = false)
    @NotNull
    @Column(name = "busy_day")
    private int busyDay;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dt_holiday")
    @Temporal(TemporalType.DATE)
    private Date dtHoliday;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dt_holiday_to")
    @Temporal(TemporalType.DATE)
    private Date dtHolidayTo;

    public DbHolidayReport1Last() {
    }

    public DbHolidayReport1Last(Integer id) {
        this.id = id;
    }

    public DbHolidayReport1Last(Integer id, String departamentAbbreviated, String post, String initials, short personNumber, int busyDay, Date dtHoliday, Date dtHolidayTo) {
        this.id = id;
        this.departamentAbbreviated = departamentAbbreviated;
        this.post = post;
        this.initials = initials;
        this.personNumber = personNumber;
        this.busyDay = busyDay;
        this.dtHoliday = dtHoliday;
        this.dtHolidayTo = dtHolidayTo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public short getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(short personNumber) {
        this.personNumber = personNumber;
    }

    public int getBusyDay() {
        return busyDay;
    }

    public void setBusyDay(int busyDay) {
        this.busyDay = busyDay;
    }

    public Date getDtHoliday() {
        return dtHoliday;
    }

    public void setDtHoliday(Date dtHoliday) {
        this.dtHoliday = dtHoliday;
    }

    public Date getDtHolidayTo() {
        return dtHolidayTo;
    }

    public void setDtHolidayTo(Date dtHolidayTo) {
        this.dtHolidayTo = dtHolidayTo;
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
        if (!(object instanceof DbHolidayReport1Last)) {
            return false;
        }
        DbHolidayReport1Last other = (DbHolidayReport1Last) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbHolidayReport1Last[ id=" + id + " ]";
    }
    
}
