package ru.kolaer.server.webportal.mvc.model.entities.general;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Danilov on 24.07.2016.
 */
@Entity
@Table(name = "general_employees")
public class GeneralEmployeesEntity {
    private int pnumber;
    private String initials;
    private EnumGender gender;
    private String departament;
    private String post;
    private GeneralAccountsEntity accountsEntity;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pnumber")
    public int getPnumber() {
        return pnumber;
    }

    public void setPnumber(int pnumber) {
        this.pnumber = pnumber;
    }

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_account", nullable = true)
    public GeneralAccountsEntity getAccountsEntity() {
        return accountsEntity;
    }

    public void setAccountsEntity(GeneralAccountsEntity accountsEntity) {
        this.accountsEntity = accountsEntity;
    }

    @Column(name = "initials")
    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    public EnumGender getGender() {
        return gender;
    }

    public void setGender(EnumGender gender) {
        this.gender = gender;
    }


    @Column(name = "departament")
    public String getDepartament() {
        return departament;
    }

    public void setDepartament(String departament) {
        this.departament = departament;
    }

    @Column(name = "post")
    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GeneralEmployeesEntity that = (GeneralEmployeesEntity) o;

        if (pnumber != that.pnumber) return false;
        if (initials != null ? !initials.equals(that.initials) : that.initials != null) return false;
        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        if (departament != null ? !departament.equals(that.departament) : that.departament != null) return false;
        if (post != null ? !post.equals(that.post) : that.post != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) pnumber;
        result = 31 * result + (initials != null ? initials.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (departament != null ? departament.hashCode() : 0);
        result = 31 * result + (post != null ? post.hashCode() : 0);
        return result;
    }
}
