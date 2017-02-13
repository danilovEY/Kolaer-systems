package ru.kolaer.api.mvp.model.kolaerweb.kolpass;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;

import java.io.Serializable;

/**
 * Created by danilovey on 20.01.2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryPasswordDto implements Serializable {
    private Integer id;
    private String name;
    private EmployeeEntity employee;
    private String urlImage;
    private RepositoryPasswordHistoryDto lastPassword;
    private RepositoryPasswordHistoryDto firstPassword;
    private RepositoryPasswordHistoryDto prevPassword;
}
