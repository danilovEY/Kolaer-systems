package ru.kolaer.server.webportal.mvc.model.entities.psr;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Immutable;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatus;
import ru.kolaer.api.mvp.model.kolaerweb.psr.PsrStatusBase;
import ru.kolaer.server.webportal.mvc.model.dao.PsrStatusDao;

import javax.persistence.*;

/**
 * Created by danilovey on 29.07.2016.
 */
@Entity
@Table(name = "psr_status")
@Immutable
@ApiModel(value="ПРС-статус", description="Статус ПСР-проекта.", subTypes = PsrStatus.class)
public class PsrStatusDecorator implements PsrStatus {
    private PsrStatus psrProjectStatus;

    public PsrStatusDecorator() {
        this.psrProjectStatus = new PsrStatusBase();
    }

    public PsrStatusDecorator(PsrStatus psrStatus) {
        this.psrProjectStatus = psrStatus;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer getId() {
        return this.psrProjectStatus.getId();
    }

    public void setId(Integer id) {
        this.psrProjectStatus.setId(id);
    }


    @Column(name = "type")
    @ApiModelProperty(value = "Наименование типа.")
    public String getType() {
        return this.psrProjectStatus.getType();
    }

    public void setType(String type) {
        this.psrProjectStatus.setType(type);
    }
}
