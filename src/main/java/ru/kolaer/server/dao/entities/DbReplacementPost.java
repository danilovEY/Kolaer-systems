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
@Table(name = "db_replacement_post", catalog = "kolaer_base", schema = "")
@XmlRootElement
public class DbReplacementPost implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Short id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "staff_list_initials")
    private String staffListInitials;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "staff_list_departament")
    private String staffListDepartament;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "staff_list_post")
    private String staffListPost;
    @Basic(optional = false)
    @NotNull
    @Column(name = "actual_miss_dt_from")
    @Temporal(TemporalType.DATE)
    private Date actualMissDtFrom;
    @Basic(optional = false)
    @NotNull
    @Column(name = "actual_miss_dt_to")
    @Temporal(TemporalType.DATE)
    private Date actualMissDtTo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "actual_miss_reason")
    private String actualMissReason;
    @Size(max = 200)
    @Column(name = "replacement_post_initials")
    private String replacementPostInitials;
    @Column(name = "replacement_post_dt_from")
    @Temporal(TemporalType.DATE)
    private Date replacementPostDtFrom;
    @Column(name = "replacement_post_dt_to")
    @Temporal(TemporalType.DATE)
    private Date replacementPostDtTo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "replacement_post_departament")
    private String replacementPostDepartament;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "replacement_post_post")
    private String replacementPostPost;

    public DbReplacementPost() {
    }

    public DbReplacementPost(Short id) {
        this.id = id;
    }

    public DbReplacementPost(Short id, String staffListInitials, String staffListDepartament, String staffListPost, Date actualMissDtFrom, Date actualMissDtTo, String actualMissReason, String replacementPostDepartament, String replacementPostPost) {
        this.id = id;
        this.staffListInitials = staffListInitials;
        this.staffListDepartament = staffListDepartament;
        this.staffListPost = staffListPost;
        this.actualMissDtFrom = actualMissDtFrom;
        this.actualMissDtTo = actualMissDtTo;
        this.actualMissReason = actualMissReason;
        this.replacementPostDepartament = replacementPostDepartament;
        this.replacementPostPost = replacementPostPost;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getStaffListInitials() {
        return staffListInitials;
    }

    public void setStaffListInitials(String staffListInitials) {
        this.staffListInitials = staffListInitials;
    }

    public String getStaffListDepartament() {
        return staffListDepartament;
    }

    public void setStaffListDepartament(String staffListDepartament) {
        this.staffListDepartament = staffListDepartament;
    }

    public String getStaffListPost() {
        return staffListPost;
    }

    public void setStaffListPost(String staffListPost) {
        this.staffListPost = staffListPost;
    }

    public Date getActualMissDtFrom() {
        return actualMissDtFrom;
    }

    public void setActualMissDtFrom(Date actualMissDtFrom) {
        this.actualMissDtFrom = actualMissDtFrom;
    }

    public Date getActualMissDtTo() {
        return actualMissDtTo;
    }

    public void setActualMissDtTo(Date actualMissDtTo) {
        this.actualMissDtTo = actualMissDtTo;
    }

    public String getActualMissReason() {
        return actualMissReason;
    }

    public void setActualMissReason(String actualMissReason) {
        this.actualMissReason = actualMissReason;
    }

    public String getReplacementPostInitials() {
        return replacementPostInitials;
    }

    public void setReplacementPostInitials(String replacementPostInitials) {
        this.replacementPostInitials = replacementPostInitials;
    }

    public Date getReplacementPostDtFrom() {
        return replacementPostDtFrom;
    }

    public void setReplacementPostDtFrom(Date replacementPostDtFrom) {
        this.replacementPostDtFrom = replacementPostDtFrom;
    }

    public Date getReplacementPostDtTo() {
        return replacementPostDtTo;
    }

    public void setReplacementPostDtTo(Date replacementPostDtTo) {
        this.replacementPostDtTo = replacementPostDtTo;
    }

    public String getReplacementPostDepartament() {
        return replacementPostDepartament;
    }

    public void setReplacementPostDepartament(String replacementPostDepartament) {
        this.replacementPostDepartament = replacementPostDepartament;
    }

    public String getReplacementPostPost() {
        return replacementPostPost;
    }

    public void setReplacementPostPost(String replacementPostPost) {
        this.replacementPostPost = replacementPostPost;
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
        if (!(object instanceof DbReplacementPost)) {
            return false;
        }
        DbReplacementPost other = (DbReplacementPost) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.kolaer.server.dao.DbReplacementPost[ id=" + id + " ]";
    }
    
}
