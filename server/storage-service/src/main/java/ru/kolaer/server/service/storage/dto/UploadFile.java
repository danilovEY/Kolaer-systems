package ru.kolaer.server.service.storage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by danilovey on 03.02.2017.
 */
@Data
@AllArgsConstructor
@ApiModel("Загруженный файл на сервер")
public class UploadFile implements Serializable{
    @ApiModelProperty("URL файла")
    private String url;
    @ApiModelProperty("Размер в битах")
    private Long size;
    @ApiModelProperty("Наименование файла")
    private String name;
}
