package ru.kolaer.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * Created by danilovey on 07.12.2016.
 */
@Data
public class PageDto<T> implements Serializable {
    private List<T> data;

    private int number = 0;

    private long total = 0;

    private int pageSize = 0;

    public PageDto() {
        this(Collections.emptyList(), 1, 0, 0);
    }

    public PageDto(List<T> data, int number, long total, int pageSize) {
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

    public PageDto(List<T> data, int number, int count, int pageSize) {
        this(data, number, (long) count, pageSize);
    }

    public static <T> PageDto<T> createPage() {
        return new PageDto<>(Collections.emptyList(), 0, 0, 0);
    }

    public static <T> PageDto<T> createPage(List<T> objects) {
        return new PageDto<>(objects, 0, 0, 0);
    }
}
