package ru.kolaer.server.webportal.mvc.model.entities.general;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by danilovey on 24.01.2017.
 */
@Entity
@Table(name = "passports")
@Data
@EqualsAndHashCode
@ApiModel("(Сотрудник) Паспорт")
public class PassportEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passports.seq")
    private Integer id;

    @OneToOne(targetEntity = EmployeeEntityDecorator.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_employee")
    @ApiModelProperty("Сотрудник")
    private EmployeeEntity employee;

    @Column(name = "serial", length = 4)
    @ApiModelProperty("Серия")
    private String serial;

    @Column(name = "number", length = 6)
    @ApiModelProperty("Номер")
    private String number;
}
