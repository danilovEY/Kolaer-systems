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
@Table(name = "bank_account")
public class BankAccount {
    @Id
    @SequenceGenerator(name = "bank_account.seq", sequenceName = "bank_account_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_account.seq")
    private Integer id;

    @ApiModelProperty("Сотрудник")
    @OneToOne(targetEntity = EmployeeEntityDecorator.class)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employeeEntity;

    @ApiModelProperty("Счет")
    @Column(name = "check", nullable = false, length = 16)
    private String check;
}
