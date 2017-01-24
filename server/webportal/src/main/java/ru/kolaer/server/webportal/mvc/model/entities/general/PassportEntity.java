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
@ApiModel("Паспорт")
public class PassportEntity implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @OneToOne(targetEntity = EmployeeEntityDecorator.class)
    @JoinColumn(name = "id_employee")
    @ApiModelProperty("Сотрудник")
    private EmployeeEntity employee;

    @Column(name = "serial", length = 2)
    @ApiModelProperty("Серия")
    private Integer serial;

    @Column(name = "number", length = 5)
    @ApiModelProperty("Номер")
    private Integer number;
}
