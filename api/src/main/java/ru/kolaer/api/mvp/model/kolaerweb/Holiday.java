package ru.kolaer.api.mvp.model.kolaerweb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Created by danilovey on 31.10.2016.
 */
@ApiModel(value = "Праздник")
public class Holiday {
    @ApiModelProperty(value = "Наименование праздника")
    private String name;

    @ApiModelProperty(value = "Дата праздника")
    private String date;

    @ApiModelProperty(value = "Тип праздника")
    private TypeDay typeDay;

    public Holiday() {}

    public Holiday(String name, String date, TypeDay typeDay) {
        this.name = name;
        this.date = date;
        this.typeDay = typeDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public TypeDay getTypeDay() {
        return typeDay;
    }

    public void setTypeDay(TypeDay typeDay) {
        this.typeDay = typeDay;
    }
}
