package ru.kolaer.api.mvp.model.kolaerweb.psr;

/**
 * Created by danilovey on 29.07.2016.
 */
public class PsrStatusBase implements PsrStatus {
    private Integer id = null;
    private String type;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
