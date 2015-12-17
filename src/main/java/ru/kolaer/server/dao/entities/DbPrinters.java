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
@Table(name = "db_printers", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbPrinters implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Short id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    private String attachment;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 16)
    @Column(name = "IP_address")
    private String iPaddress;
    @Size(max = 60)
    @Column(name = "network_name")
    private String networkName;

    public DbPrinters() {
    }

    public DbPrinters(Short id) {
        this.id = id;
    }

    public DbPrinters(Short id, String name, String attachment, String iPaddress) {
        this.id = id;
        this.name = name;
        this.attachment = attachment;
        this.iPaddress = iPaddress;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public String getIPaddress() {
        return iPaddress;
    }

    public void setIPaddress(String iPaddress) {
        this.iPaddress = iPaddress;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
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
        if (!(object instanceof DbPrinters)) {
            return false;
        }
        DbPrinters other = (DbPrinters) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbPrinters[ id=" + id + " ]";
    }
    
}
