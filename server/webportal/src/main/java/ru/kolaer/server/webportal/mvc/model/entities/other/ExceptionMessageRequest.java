package ru.kolaer.server.webportal.mvc.model.entities.other;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

/**
 * Created by danilovey on 10.11.2016.
 */
@ApiModel(value = "Информация об ошибки")
public class ExceptionMessageRequest {
    @ApiModelProperty(value = "Сообщение")
    private String message;

    @ApiModelProperty(value = "Код ошибки")
    private String status;

    @ApiModelProperty(value = "Дата ошибки")
    private Date date;

    public ExceptionMessageRequest(String message, String status, Date date) {
        this.message = message;
        this.status = status;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonFormat(locale = "ru", shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Moscow")
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ExceptionMessageRequest{" +
                "message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", date=" + date +
                '}';
    }
}
