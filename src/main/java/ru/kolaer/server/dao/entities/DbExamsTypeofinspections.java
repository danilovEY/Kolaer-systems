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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Danilov
 */
@Entity
@Table(name = "db_exams_typeofinspections", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbExamsTypeofinspections implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Short id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "type_of_inspection")
    private String typeOfInspection;

    public DbExamsTypeofinspections() {
    }

    public DbExamsTypeofinspections(Short id) {
        this.id = id;
    }

    public DbExamsTypeofinspections(Short id, String typeOfInspection) {
        this.id = id;
        this.typeOfInspection = typeOfInspection;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getTypeOfInspection() {
        return typeOfInspection;
    }

    public void setTypeOfInspection(String typeOfInspection) {
        this.typeOfInspection = typeOfInspection;
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
        if (!(object instanceof DbExamsTypeofinspections)) {
            return false;
        }
        DbExamsTypeofinspections other = (DbExamsTypeofinspections) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbExamsTypeofinspections[ id=" + id + " ]";
    }
    
}
