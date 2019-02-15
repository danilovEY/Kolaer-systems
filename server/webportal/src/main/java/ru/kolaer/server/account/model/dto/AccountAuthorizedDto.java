package ru.kolaer.server.account.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AccountAuthorizedDto implements UserDetails, CredentialsContainer {
    private final long id;
    private final String username;
    private String password;
    private Long employeeId;
    private final Collection<GrantedAuthority> authorities;
    private final boolean accountNonExpired = true;
    private final boolean accountNonLocked;
    private boolean credentialsNonExpired = true;
    private final boolean enabled = true;

    public AccountAuthorizedDto(long id, String username, String password, Long employeeId, Collection<String> accessNames, boolean accountNonLocked) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.employeeId = employeeId;
        this.accountNonLocked = accountNonLocked;
        this.authorities = accessNames
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
        this.credentialsNonExpired = false;
    }

    @JsonIgnore
    public boolean hasAccess(String access) {
        return authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(access::equals);
    }

    @JsonIgnore
    public boolean hasAnyAccess(String... accesses) {
        List<String> accessList = Arrays.asList(accesses);

        return authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(accessList::contains);
    }
}
