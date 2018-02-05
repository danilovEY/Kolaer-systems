package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by danilovey on 01.12.2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdDto implements BaseDto{
    private Long id;
}
