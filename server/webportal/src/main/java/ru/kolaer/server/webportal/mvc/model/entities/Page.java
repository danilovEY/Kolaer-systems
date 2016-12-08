package ru.kolaer.server.webportal.mvc.model.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by danilovey on 07.12.2016.
 */
@Data
@ApiModel("Страница с данными")
public class Page<T> implements Serializable {
    @ApiModelProperty("Список объектов")
    private List<T> data;

    @ApiModelProperty("Номер страници")
    private int number = 0;

    @ApiModelProperty("Общее кол-во объектов")
    private int total = 0;

    @ApiModelProperty("Кол-во объектов на странице")
    private int pageSize = 0;

    public Page(List<T> data, int number, int total, int pageSize) {
        this.data = data;
        this.number = number;
        if(number == 0){
            this.total = 0;
            this.pageSize = data.size();
        } else {
            this.total = total;
            this.pageSize = pageSize;
        }
    }

    public Page(List<T> data, int number, Long count, int pageSize) {
        this(data, number, count.intValue(), pageSize);
    }
}
