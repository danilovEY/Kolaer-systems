package ru.kolaer.api.mvp.model.kolaerweb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by danilovey on 07.12.2016.
 */
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

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
