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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "db_holiday_sync", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbHolidaySync implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Short id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "last_dt")
    @Temporal(TemporalType.DATE)
    private Date lastDt;
    @Basic(optional = false)
    @NotNull
    @Column(name = "next_dt")
    @Temporal(TemporalType.DATE)
    private Date nextDt;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 8)
    private String status;

    public DbHolidaySync() {
    }

    public DbHolidaySync(Short id) {
        this.id = id;
    }

    public DbHolidaySync(Short id, Date lastDt, Date nextDt, String status) {
        this.id = id;
        this.lastDt = lastDt;
        this.nextDt = nextDt;
        this.status = status;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public Date getLastDt() {
        return lastDt;
    }

    public void setLastDt(Date lastDt) {
        this.lastDt = lastDt;
    }

    public Date getNextDt() {
        return nextDt;
    }

    public void setNextDt(Date nextDt) {
        this.nextDt = nextDt;
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
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DbHolidaySync)) {
            return false;
        }
        DbHolidaySync other = (DbHolidaySync) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbHolidaySync[ id=" + id + " ]";
    }
    
}
