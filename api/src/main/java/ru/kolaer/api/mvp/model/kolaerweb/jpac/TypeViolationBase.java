package ru.kolaer.api.mvp.model.kolaerweb.jpac;

/**
 * Created by danilovey on 06.09.2016.
 */
public class TypeViolationBase implements TypeViolation{
    private Integer id;
    private String name;

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
}
