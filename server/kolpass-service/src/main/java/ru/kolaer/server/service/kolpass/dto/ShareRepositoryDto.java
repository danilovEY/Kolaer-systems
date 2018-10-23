package ru.kolaer.server.service.kolpass.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by danilovey on 30.11.2016.
 */
@Data
public class ShareRepositoryDto {
    private List<Long> accountIds;
}
