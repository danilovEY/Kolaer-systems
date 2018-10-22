package ru.kolaer.server.webportal.microservice.account.pojo.dto;

import lombok.Data;

@Data
public class ChangePasswordDto {
    private String oldPassword;
    private String newPassword;
}
