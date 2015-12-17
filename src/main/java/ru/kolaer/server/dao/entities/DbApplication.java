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
@Table(name = "db_application", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbApplication implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title_app")
    private String titleApp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status_app")
    private boolean statusApp;

    public DbApplication() {
    }

    public DbApplication(Integer id) {
        this.id = id;
    }

    public DbApplication(Integer id, String titleApp, boolean statusApp) {
        this.id = id;
        this.titleApp = titleApp;
        this.statusApp = statusApp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitleApp() {
        return titleApp;
    }

    public void setTitleApp(String titleApp) {
        this.titleApp = titleApp;
    }

    public boolean getStatusApp() {
        return statusApp;
    }

    public void setStatusApp(boolean statusApp) {
        this.statusApp = statusApp;
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
        if (!(object instanceof DbApplication)) {
            return false;
        }
        DbApplication other = (DbApplication) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbApplication[ id=" + id + " ]";
    }
    
}
