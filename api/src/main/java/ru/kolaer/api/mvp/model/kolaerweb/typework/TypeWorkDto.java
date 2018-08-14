package ru.kolaer.api.mvp.model.kolaerweb.typework;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

@Data
@NoArgsConstructor
public class TypeWorkDto implements BaseDto {
    private Long id;
    private String name;

    public TypeWorkDto(Long id) {
        this.id = id;
    }
}
