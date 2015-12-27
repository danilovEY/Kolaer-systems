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
@Table(name = "db_remark_walking", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbRemarkWalking implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Short id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    private String applicant;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_applicant")
    private short idApplicant;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_create_remark")
    @Temporal(TemporalType.DATE)
    private Date dateCreateRemark;
    @Basic(optional = false)
    @NotNull
    private short cipher;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    private String performer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "id_performer")
    private short idPerformer;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    private String departament;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    private String content;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "content_action")
    private String contentAction;
    @Basic(optional = false)
    @NotNull
    @Column(name = "date_term")
    @Temporal(TemporalType.DATE)
    private Date dateTerm;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 21)
    private String status;

    public DbRemarkWalking() {
    }

    public DbRemarkWalking(Short id) {
        this.id = id;
    }

    public DbRemarkWalking(Short id, String applicant, short idApplicant, Date dateCreateRemark, short cipher, String performer, short idPerformer, String departament, String content, String contentAction, Date dateTerm, String status) {
        this.id = id;
        this.applicant = applicant;
        this.idApplicant = idApplicant;
        this.dateCreateRemark = dateCreateRemark;
        this.cipher = cipher;
        this.performer = performer;
        this.idPerformer = idPerformer;
        this.departament = departament;
        this.content = content;
        this.contentAction = contentAction;
        this.dateTerm = dateTerm;
        this.status = status;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getApplicant() {
        return applicant;
    }

    public void setApplicant(String applicant) {
        this.applicant = applicant;
    }

    public short getIdApplicant() {
        return idApplicant;
    }

    public void setIdApplicant(short idApplicant) {
        this.idApplicant = idApplicant;
    }

    public Date getDateCreateRemark() {
        return dateCreateRemark;
    }

    public void setDateCreateRemark(Date dateCreateRemark) {
        this.dateCreateRemark = dateCreateRemark;
    }

    public short getCipher() {
        return cipher;
    }

    public void setCipher(short cipher) {
        this.cipher = cipher;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public short getIdPerformer() {
        return idPerformer;
    }

    public void setIdPerformer(short idPerformer) {
        this.idPerformer = idPerformer;
    }

    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentAction() {
        return contentAction;
    }

    public void setContentAction(String contentAction) {
        this.contentAction = contentAction;
    }

    public Date getDateTerm() {
        return dateTerm;
    }

    public void setDateTerm(Date dateTerm) {
        this.dateTerm = dateTerm;
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
        if (!(object instanceof DbRemarkWalking)) {
            return false;
        }
        DbRemarkWalking other = (DbRemarkWalking) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbRemarkWalking[ id=" + id + " ]";
    }
    
}
