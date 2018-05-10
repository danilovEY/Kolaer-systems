package ru.kolaer.api.mvp.model.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnexpectedParamsDescription {
    private String field;
    private String message;
}
