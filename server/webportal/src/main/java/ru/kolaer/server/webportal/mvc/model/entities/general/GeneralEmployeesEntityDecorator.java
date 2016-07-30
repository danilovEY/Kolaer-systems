package ru.kolaer.server.webportal.mvc.model.entities.general;

import ru.kolaer.api.mvp.model.kolaerweb.EnumGender;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralAccountsEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntityBase;

import javax.persistence.*;

/**
 * Created by Danilov on 24.07.2016.
 * Структура сотрудника в БД.
 */
@Entity
@Table(name = "general_employees")
public class GeneralEmployeesEntityDecorator implements GeneralEmployeesEntity {
    private GeneralEmployeesEntity generalEmployeesEntity;

    public GeneralEmployeesEntityDecorator() {
        this.generalEmployeesEntity = new GeneralEmployeesEntityBase();
    }

    public GeneralEmployeesEntityDecorator(GeneralEmployeesEntity generalEmployeesEntity) {
        this.generalEmployeesEntity = generalEmployeesEntity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pnumber")
    public int getPnumber() {
        return this.generalEmployeesEntity.getPnumber();
    }

    public void setPnumber(int pnumber) {
        this.generalEmployeesEntity.setPnumber(pnumber);
    }

    @OneToOne(targetEntity = GeneralAccountsEntityDecorator.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "id_account", nullable = true)
    public GeneralAccountsEntity getAccountsEntity() {
        return this.generalEmployeesEntity.getAccountsEntity();
    }

    public void setAccountsEntity(GeneralAccountsEntity accountsEntity) {
        this.generalEmployeesEntity.setAccountsEntity(accountsEntity);
    }

    @Column(name = "initials")
    public String getInitials() {
        return this.generalEmployeesEntity.getInitials();
    }

    public void setInitials(String initials) {
        this.generalEmployeesEntity.setInitials(initials);
    }

    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    public EnumGender getGender() {
        return this.generalEmployeesEntity.getGender();
    }

    public void setGender(EnumGender gender) {
        this.generalEmployeesEntity.setGender(gender);
    }


    @Column(name = "departament")
    public String getDepartament() {
        return this.generalEmployeesEntity.getDepartament();
    }

    public void setDepartament(String departament) {
        this.generalEmployeesEntity.setDepartament(departament);
    }

    @Column(name = "post")
    public String getPost() {
        return this.generalEmployeesEntity.getPost();
    }

    public void setPost(String post) {
        this.generalEmployeesEntity.setPost(post);
    }

    @Override
    public boolean equals(Object o) {
        return this.generalEmployeesEntity.equals(o);
    }

    @Override
    public int hashCode() {
        return this.generalEmployeesEntity.hashCode();
    }
}