package ru.kolaer.api.mvp.model.kolaerweb;

/**
 * Created by danilovey on 12.09.2016.
 */
public class DepartmentEntityBase implements DepartmentEntity {
    private Integer Id;
    private String name;
    private String abbreviatedName;
    private Integer idChiefEntity;

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

    public Integer getChiefEntity() {
        return idChiefEntity;
    }

    public void setChiefEntity(Integer chiefEntity) {
        this.idChiefEntity = chiefEntity;
    }

    @Override
    public String toString() {
        return "DepartmentEntityBase{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", abbreviatedName='" + abbreviatedName + '\'' +
                ", idChiefEntity=" + idChiefEntity +
                '}';
    }
}
