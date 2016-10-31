package ru.kolaer.api.mvp.model.kolaerweb;

/**
 * Created by danilovey on 31.10.2016.
 */
public class Holiday {
    private String name;
    private DateTimeJson dateTimeHoliday;
    private TypeDay typeDay;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateTimeJson getDateTimeHoliday() {
        return dateTimeHoliday;
    }

    public void setDateTimeHoliday(DateTimeJson dateTimeHoliday) {
        this.dateTimeHoliday = dateTimeHoliday;
    }

    public TypeDay getTypeDay() {
        return typeDay;
    }

    public void setTypeDay(TypeDay typeDay) {
        this.typeDay = typeDay;
    }
}
