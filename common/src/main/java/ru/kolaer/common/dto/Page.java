package ru.kolaer.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by danilovey on 07.12.2016.
 */
@Data
public class Page<T> implements Serializable {
    private List<T> data;

    private int number = 0;

    private int total = 0;

    private int pageSize = 0;

    public Page() {
        this(Collections.emptyList(), 1, 0, 0);
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
