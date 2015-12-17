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
@Table(name = "db_organization_askid", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbOrganizationAskid implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_organization")
    private short idOrganization;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    private String name;

    public DbOrganizationAskid() {
    }

    public DbOrganizationAskid(Integer id) {
        this.id = id;
    }

    public DbOrganizationAskid(Integer id, short idOrganization, String name) {
        this.id = id;
        this.idOrganization = idOrganization;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public short getIdOrganization() {
        return idOrganization;
    }

    public void setIdOrganization(short idOrganization) {
        this.idOrganization = idOrganization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        if (!(object instanceof DbOrganizationAskid)) {
            return false;
        }
        DbOrganizationAskid other = (DbOrganizationAskid) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbOrganizationAskid[ id=" + id + " ]";
    }
    
}
