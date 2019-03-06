package ru.kolaer.server.core.converter;

import org.springframework.data.domain.Page;
import ru.kolaer.common.dto.PageDto;

import java.util.List;
import java.util.function.Function;

public class CommonConverter {

    public static <T> PageDto<T> toPageDto(Page<T> page) {
        return new PageDto<>(
                page.getContent(),
                page.getNumber(),
                page.getTotalElements(),
                page.getSize()
        );
    }

    public static <T, V> PageDto<T> toPageDto(Page<V> page, Function<List<V>, List<T>> converter) {
        return new PageDto<>(
                converter.apply(page.getContent()),
                page.getNumber(),
                page.getTotalElements(),
                page.getSize()
        );
    }

}
