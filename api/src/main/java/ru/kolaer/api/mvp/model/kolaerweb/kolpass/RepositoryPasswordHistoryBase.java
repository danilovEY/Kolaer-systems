package ru.kolaer.api.mvp.model.kolaerweb.kolpass;

import lombok.*;

import java.util.Date;

/**
 * Created by danilovey on 20.01.2017.
 */
@AllArgsConstructor
@NoArgsConstructor
public class RepositoryPasswordHistoryBase implements RepositoryPasswordHistory {
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    private String login;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private Date passwordWriteDate;

    @Getter
    @Setter
    private RepositoryPassword repositoryPassword;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RepositoryPasswordHistory that = (RepositoryPasswordHistory) o;

        if (id != null ? !id.equals(that.getId()) : that.getId()!= null) return false;
        if (login != null ? !login.equals(that.getLogin()) : that.getLogin() != null) return false;
        if (password != null ? !password.equals(that.getPassword()) : that.getPassword()!= null) return false;
        if (passwordWriteDate != null ? !passwordWriteDate.equals(that.getPasswordWriteDate()) : that.getPasswordWriteDate()!= null)
            return false;
        return repositoryPassword != null ? repositoryPassword.getId().equals(that.getRepositoryPassword().getId()) : that.getRepositoryPassword() == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (passwordWriteDate != null ? passwordWriteDate.hashCode() : 0);
        return result;
    }
}
