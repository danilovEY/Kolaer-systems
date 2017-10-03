package ru.kolaer.server.webportal.mvc.model.entities.general;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;

import javax.persistence.*;

/**
 * Created by danilovey on 13.12.2016.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("(Банк) Данные банка")
@Entity
@Table(name = "bank_account")
public class BankAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @ApiModelProperty("Сотрудник")
    @OneToOne(targetEntity = EmployeeEntityDecorator.class)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employeeEntity;

    @ApiModelProperty("Счет")
    @Column(name = "check", nullable = false, length = 16)
    private String check;
}
