package ru.kolaer.server.webportal.model.dto.account;

import lombok.Data;

@Data
public class ChangePasswordDto {
    private String oldPassword;
    private String newPassword;
}
