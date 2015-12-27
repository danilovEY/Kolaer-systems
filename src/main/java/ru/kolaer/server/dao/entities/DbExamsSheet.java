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
import javax.persistence.Lob;
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
@Table(name = "db_exams_sheet", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbExamsSheet implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Short id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "person_number")
    private short personNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "type_of_inspection")
    private String typeOfInspection;
    @Column(name = "dt_of_inspection_last")
    @Temporal(TemporalType.DATE)
    private Date dtOfInspectionLast;
    @Basic(optional = false)
    @NotNull
    @Column(name = "dt_of_inspection")
    @Temporal(TemporalType.DATE)
    private Date dtOfInspection;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "num_certificate")
    private String numCertificate;
    @Size(max = 20)
    @Column(name = "num_protocol")
    private String numProtocol;
    @Size(max = 4)
    @Column(name = "group_electrical")
    private String groupElectrical;
    @Lob
    @Size(max = 65535)
    private String notes;

    public DbExamsSheet() {
    }

    public DbExamsSheet(Short id) {
        this.id = id;
    }

    public DbExamsSheet(Short id, short personNumber, String typeOfInspection, Date dtOfInspection, String numCertificate) {
        this.id = id;
        this.personNumber = personNumber;
        this.typeOfInspection = typeOfInspection;
        this.dtOfInspection = dtOfInspection;
        this.numCertificate = numCertificate;
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

    public String getTypeOfInspection() {
        return typeOfInspection;
    }

    public void setTypeOfInspection(String typeOfInspection) {
        this.typeOfInspection = typeOfInspection;
    }

    public Date getDtOfInspectionLast() {
        return dtOfInspectionLast;
    }

    public void setDtOfInspectionLast(Date dtOfInspectionLast) {
        this.dtOfInspectionLast = dtOfInspectionLast;
    }

    public Date getDtOfInspection() {
        return dtOfInspection;
    }

    public void setDtOfInspection(Date dtOfInspection) {
        this.dtOfInspection = dtOfInspection;
    }

    public String getNumCertificate() {
        return numCertificate;
    }

    public void setNumCertificate(String numCertificate) {
        this.numCertificate = numCertificate;
    }

    public String getNumProtocol() {
        return numProtocol;
    }

    public void setNumProtocol(String numProtocol) {
        this.numProtocol = numProtocol;
    }

    public String getGroupElectrical() {
        return groupElectrical;
    }

    public void setGroupElectrical(String groupElectrical) {
        this.groupElectrical = groupElectrical;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
        if (!(object instanceof DbExamsSheet)) {
            return false;
        }
        DbExamsSheet other = (DbExamsSheet) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbExamsSheet[ id=" + id + " ]";
    }
    
}
