package ru.kolaer.api.mvp.model.kolaerweb;

/**
 * Created by danilovey on 12.09.2016.
 */
public class GeneralDepartamentEntityBase implements GeneralDepartamentEntity {
    private Integer Id;
    private String name;
    private String abbreviatedName;
    private GeneralEmployeesEntity chiefEntity;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviatedName() {
        return abbreviatedName;
    }

    public void setAbbreviatedName(String abbreviatedName) {
        this.abbreviatedName = abbreviatedName;
    }

    public GeneralEmployeesEntity getChiefEntity() {
        return chiefEntity;
    }

    public void setChiefEntity(GeneralEmployeesEntity chiefEntity) {
        this.chiefEntity = chiefEntity;
    }
}
