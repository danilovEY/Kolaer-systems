package ru.kolaer.api.mvp.model.kolaerweb.kolpass;

import lombok.*;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeEntity;

import java.util.List;

/**
 * Created by danilovey on 20.01.2017.
 */
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class RepositoryPasswordBase implements RepositoryPassword {
    @Getter @Setter
    private Integer id;

    @Getter @Setter
    private String name;

    @Getter @Setter
    private EmployeeEntity employee;

    @Getter @Setter
    private String urlImage;

    @Getter @Setter
    private RepositoryPasswordHistory lastPassword;

    @Getter @Setter
    private RepositoryPasswordHistory firstPassword;

    @Getter @Setter
    private RepositoryPasswordHistory prevPassword;

    @Getter @Setter
    private List<RepositoryPasswordHistory> historyPasswords;
}
