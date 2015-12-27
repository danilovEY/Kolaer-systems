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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Danilov
 */
@Entity
@Table(name = "db_medcontrol_option", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbMedcontrolOption implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dt_value")
    @Temporal(TemporalType.DATE)
    private Date dtValue;

    public DbMedcontrolOption() {
    }

    public DbMedcontrolOption(Integer id) {
        this.id = id;
    }

    public DbMedcontrolOption(Integer id, Date dtValue) {
        this.id = id;
        this.dtValue = dtValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDtValue() {
        return dtValue;
    }

    public void setDtValue(Date dtValue) {
        this.dtValue = dtValue;
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
        if (!(object instanceof DbMedcontrolOption)) {
            return false;
        }
        DbMedcontrolOption other = (DbMedcontrolOption) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbMedcontrolOption[ id=" + id + " ]";
    }
    
}
