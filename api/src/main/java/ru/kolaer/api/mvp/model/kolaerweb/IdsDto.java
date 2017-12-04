package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * Created by danilovey on 01.12.2017.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdsDto {
    private List<Long> ids;

    public IdsDto(Long... ids) {
        this.ids = Arrays.asList(ids);
    }
}
