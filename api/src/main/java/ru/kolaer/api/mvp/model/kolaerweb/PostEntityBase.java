package ru.kolaer.api.mvp.model.kolaerweb;

/**
 * Created by danilovey on 24.01.2017.
 */
public class PostEntityBase implements PostEntity {
    private Integer id;
    private String name;
    private String abbreviatedName;
    private String typeRang;
    private Integer rang;

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAbbreviatedName() {
        return this.abbreviatedName;
    }

    @Override
    public void setAbbreviatedName(String abbreviatedName) {
        this.abbreviatedName = abbreviatedName;
    }

    @Override
    public String getTypeRang() {
        return this.typeRang;
    }

    @Override
    public void setTypeRang(String typeRang) {
        this.typeRang = typeRang;
    }

    @Override
    public Integer getRang() {
        return this.rang;
    }

    @Override
    public void setRang(Integer rang) {
        this.rang = rang;
    }
}
