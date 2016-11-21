package ru.kolaer.api.mvp.model.kolaerweb.psr;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * Created by danilovey on 29.07.2016.
 */
@JsonDeserialize(as = PsrAttachmentBase.class)
@ApiModel(value="ПРС-вложения", description="Ссылки на ПСР-вложения.")
public interface PsrAttachment extends Serializable {
    Integer getId();
    void setId(Integer id);

    @ApiModelProperty(value = "Имя вложения")
    String getName();
    void setName(String name);

    @ApiModelProperty(value = "Путь к вложению")
    String getPathFile();
    void setPathFile(String pathFile);
}
