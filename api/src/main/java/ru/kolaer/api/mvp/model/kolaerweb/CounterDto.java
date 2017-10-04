package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.Data;

import java.util.Date;

/**
 * Created by danilovey on 25.08.2016.
 */
@Data
public class CounterDto implements BaseDto{
    private Long id;
    private Date start;
    private Date end;
    private String title;
    private String description;
}
