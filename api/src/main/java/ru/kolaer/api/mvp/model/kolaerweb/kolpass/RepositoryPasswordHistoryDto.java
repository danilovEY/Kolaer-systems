package ru.kolaer.api.mvp.model.kolaerweb.kolpass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by danilovey on 20.01.2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryPasswordHistoryDto implements Serializable {
    private Long id;
    private String login;
    private String password;
    private Date passwordWriteDate;
    private RepositoryPasswordDto repositoryPasswordDto;
}
