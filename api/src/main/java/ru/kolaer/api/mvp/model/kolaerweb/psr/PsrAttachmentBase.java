package ru.kolaer.api.mvp.model.kolaerweb.psr;

/**
 * Created by danilovey on 29.07.2016.
 */
public class PsrAttachmentBase implements PsrAttachment {
    private Integer id = null;
    private String pathFile;
    private String name;
    private PsrRegister psrRegister;

    public Integer getId() {
        return id;
    }

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

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

}
