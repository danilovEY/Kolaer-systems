package ru.kolaer.server.webportal.mvc.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by danilovey on 26.01.2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel("Результат обновления сотрудников")
public class ResultUpdateEmployeesDto implements Serializable {
    @ApiModelProperty("Общее кол-во должностей")
    private Integer allPostSize;
    @ApiModelProperty("Кол-во удаленных должностей")
    private Integer deletePostCount;
    @ApiModelProperty("Кол-во добавленных должностей")
    private Integer addPostCount;

    @ApiModelProperty("Общее кол-во подразделений")
    private Integer allDepSize;
    @ApiModelProperty("Кол-во удаленных подразделений")
    private Integer deleteDepCount;
    @ApiModelProperty("Кол-во добавленных подразделений")
    private Integer addDepCount;

    @ApiModelProperty("Общее кол-во сотрудников")
    private Integer allEmployeeSize;
    @ApiModelProperty("Кол-во уволенных сотрудников")
    private Integer deleteEmployeeCount;
    @ApiModelProperty("Кол-во добавленных сотрудников")
    private Integer addEmployeeCount;

}
