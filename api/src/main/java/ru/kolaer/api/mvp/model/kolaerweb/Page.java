package ru.kolaer.api.mvp.model.kolaerweb;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by danilovey on 07.12.2016.
 */
@ApiModel("Страница с данными")
@Data
public class Page<T> implements Serializable {
    @ApiModelProperty("Список объектов")
    private List<T> data;

    @ApiModelProperty("Номер страници")
    private int number = 0;

    @ApiModelProperty("Общее кол-во объектов")
    private int total = 0;

    @ApiModelProperty("Кол-во объектов на странице")
    private int pageSize = 0;

    public Page() {

    }

    public Page(List<T> data, int number, int total, int pageSize) {
        this.data = data;
        this.number = number;
        if(pageSize == 0){
            this.pageSize = data.size();
            this.total = this.pageSize;
        } else {
            this.total = total;
            this.pageSize = pageSize;
        }
    }

    public Page(List<T> data, int number, Long count, int pageSize) {
        this(data, number, count.intValue(), pageSize);
    }

    public static <T> Page<T> createPage() {
        return new Page<>(Collections.emptyList(), 0, 0, 0);
    }

    public static <T> Page<T> createPage(List<T> objects) {
        return new Page<>(objects, 0, 0, 0);
    }
}
