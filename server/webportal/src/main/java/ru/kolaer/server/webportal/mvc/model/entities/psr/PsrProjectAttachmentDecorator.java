package ru.kolaer.server.webportal.mvc.model.entities.psr;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrProjectAttachment;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrProjectAttachmentBase;

/**
 * Created by danilovey on 29.07.2016.
 */
public class PsrProjectAttachmentDecorator implements PsrProjectAttachment{
    private PsrProjectAttachmentBase psrProjectAttachment;

    public PsrProjectAttachmentDecorator() {
        this.psrProjectAttachment = new PsrProjectAttachmentBase();
    }

    public int getId() {
        return this.psrProjectAttachment.getId();
    }

    public void setId(int id) {
        this.psrProjectAttachment.setId(id);
    }

    public String getPathFile() {
        return this.psrProjectAttachment.getPathFile();
    }

    public void setPathFile(String pathFile) {
        this.psrProjectAttachment.setPathFile(pathFile);
    }
}
