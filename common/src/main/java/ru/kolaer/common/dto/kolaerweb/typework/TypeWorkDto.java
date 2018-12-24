package ru.kolaer.common.dto.kolaerweb.typework;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.common.dto.BaseDto;

@Data
@NoArgsConstructor
public class TypeWorkDto implements BaseDto {
    private Long id;
    private String name;

    public TypeWorkDto(Long id) {
        this.id = id;
    }
}
