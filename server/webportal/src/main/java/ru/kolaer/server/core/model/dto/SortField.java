package ru.kolaer.server.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
public class SortField {
    @Getter
    @Setter
    private String sortField;

    @Getter
    @Setter
    private SortType sortType;

    @Override
    public String toString() {
        return String.format("ORDER BY %s %s", Optional.ofNullable(sortField).orElse("id"),
                Optional.ofNullable(sortType).orElse(SortType.ASC));
    }
}