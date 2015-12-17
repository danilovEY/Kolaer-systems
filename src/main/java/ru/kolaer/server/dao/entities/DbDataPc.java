/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kolaer.server.dao.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Danilov
 */
@MappedSuperclass
@Table(name = "db_data_pc", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbDataPc implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Short id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PC_name")
    private String pCname;
    @Column(name = "person_number")
    private Short personNumber;
    @Size(max = 70)
    private String user;
    @Size(max = 100)
    @Column(name = "departament_abbreviated")
    private String departamentAbbreviated;
    @Size(max = 200)
    private String monitor;
    @Size(max = 100)
    @Column(name = "inv_monitor")
    private String invMonitor;
    @Size(max = 200)
    private String tower;
    @Size(max = 100)
    @Column(name = "inv_tower")
    private String invTower;
    @Size(max = 100)
    private String windows;
    @Size(max = 100)
    private String office;
    @Size(max = 100)
    private String visio;
    @Size(max = 100)
    private String project;
    @Size(max = 100)
    @Column(name = "1C")
    private String c;
    @Size(max = 100)
    private String smeta;

    public DbDataPc() {
    }

    public DbDataPc(Short id) {
        this.id = id;
    }

    public DbDataPc(Short id, String pCname) {
        this.id = id;
        this.pCname = pCname;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getPCname() {
        return pCname;
    }

    public void setPCname(String pCname) {
        this.pCname = pCname;
    }

    public Short getPersonNumber() {
        return personNumber;
    }

    public void setPersonNumber(Short personNumber) {
        this.personNumber = personNumber;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDepartamentAbbreviated() {
        return departamentAbbreviated;
    }

    public void setDepartamentAbbreviated(String departamentAbbreviated) {
        this.departamentAbbreviated = departamentAbbreviated;
    }

    public String getMonitor() {
        return monitor;
    }

    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }

    public String getInvMonitor() {
        return invMonitor;
    }

    public void setInvMonitor(String invMonitor) {
        this.invMonitor = invMonitor;
    }

    public String getTower() {
        return tower;
    }

    public void setTower(String tower) {
        this.tower = tower;
    }

    public String getInvTower() {
        return invTower;
    }

    public void setInvTower(String invTower) {
        this.invTower = invTower;
    }

    public String getWindows() {
        return windows;
    }

    public void setWindows(String windows) {
        this.windows = windows;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getVisio() {
        return visio;
    }

    public void setVisio(String visio) {
        this.visio = visio;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }

    public String getSmeta() {
        return smeta;
    }

    public void setSmeta(String smeta) {
        this.smeta = smeta;
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
        if (!(object instanceof DbDataPc)) {
            return false;
        }
        DbDataPc other = (DbDataPc) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbDataPc[ id=" + id + " ]";
    }
    
}
