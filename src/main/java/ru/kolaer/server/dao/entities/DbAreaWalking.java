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
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Danilov
 */
@Entity
@Table(name = "db_area_walking", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbAreaWalking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Short cipher;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "scan_area")
    private String scanArea;

    public DbAreaWalking() {
    }

    public DbAreaWalking(Short cipher) {
        this.cipher = cipher;
    }

    public DbAreaWalking(Short cipher, String scanArea) {
        this.cipher = cipher;
        this.scanArea = scanArea;
    }

    public Short getCipher() {
        return cipher;
    }

    public void setCipher(Short cipher) {
        this.cipher = cipher;
    }

    public String getScanArea() {
        return scanArea;
    }

    public void setScanArea(String scanArea) {
        this.scanArea = scanArea;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cipher != null ? cipher.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DbAreaWalking)) {
            return false;
        }
        DbAreaWalking other = (DbAreaWalking) object;
        if ((this.cipher == null && other.cipher != null) || (this.cipher != null && !this.cipher.equals(other.cipher))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbAreaWalking[ cipher=" + cipher + " ]";
    }
    
}
