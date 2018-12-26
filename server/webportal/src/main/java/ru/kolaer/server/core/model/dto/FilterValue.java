package ru.kolaer.server.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterValue {
    private String paramName;
    private Object value;
    private FilterType type = FilterType.LIKE;

    public FilterValue(Object value) {
        this.value = value;
    }
}
