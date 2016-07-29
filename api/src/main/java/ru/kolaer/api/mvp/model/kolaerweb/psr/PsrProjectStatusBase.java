package ru.kolaer.api.mvp.model.kolaerweb.psr;

/**
 * Created by danilovey on 29.07.2016.
 */
public class PsrProjectStatusBase implements PsrProjectStatus {
    private int id;
    private String type;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
