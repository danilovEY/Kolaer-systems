package ru.kolaer.server.webportal.mvc.model.entities.psr;

import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrAttachment;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrAttachmentBase;

import javax.persistence.*;

/**
 * Created by danilovey on 29.07.2016.
 */
@Entity
@Table(name = "psr_attachment")
public class PsrAttachmentDecorator implements PsrAttachment {
    private PsrAttachment psrAttachment;

    public PsrAttachmentDecorator() {
        this.psrAttachment = new PsrAttachmentBase();
    }

    public PsrAttachmentDecorator(PsrAttachment psrAttachment) {
        this.psrAttachment = psrAttachment;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Integer getId() {
        return this.psrAttachment.getId();
    }


    public void setId(Integer id) {
        this.psrAttachment.setId(id);
    }


    @Column(name = "name")
    public String getName() {
        return this.psrAttachment.getName();
    }

    public void setName(String name) {
        this.psrAttachment.setName(name);
    }

    @Column(name = "path_file")
    public String getPathFile() {
        return this.psrAttachment.getPathFile();
    }


    public void setPathFile(String pathFile) {
        this.psrAttachment.setPathFile(pathFile);
    }

}
