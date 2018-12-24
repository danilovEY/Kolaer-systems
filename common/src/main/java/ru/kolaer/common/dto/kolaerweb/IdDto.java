package ru.kolaer.common.dto.kolaerweb;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.common.dto.BaseDto;

/**
 * Created by danilovey on 01.12.2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdDto implements BaseDto {
    private Long id;
}
