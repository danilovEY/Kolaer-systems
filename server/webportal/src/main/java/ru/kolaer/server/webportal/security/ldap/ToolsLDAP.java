package ru.kolaer.server.webportal.security.ldap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ru.kolaer.api.mvp.model.kolaerweb.EnumRole;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by danilovey on 30.08.2016.
 */
@Component
public class ToolsLDAP {
    private final Logger LOG = LoggerFactory.getLogger(ToolsLDAP.class);

    public Collection<? extends GrantedAuthority> getRolesFromAttributes(Attributes attributes) {
        LOG.debug("Attributes: {}", attributes);
        final List<GrantedAuthority> roles = new ArrayList<>();
        try {
            final Attribute cn = attributes.get("memberOf");
            for(int i = 0; i< cn.size(); i++) {
                final String value = (String) cn.get(i);
                final String group = value.substring(3, value.indexOf(','));
                final SimpleGrantedAuthority simpleGrantedAuthority = this.getRole(group);
                if(simpleGrantedAuthority != null)
                    roles.add(simpleGrantedAuthority);
            }
        } catch (NamingException e) {
            LOG.error("Ошибка при парсинге атрибутов!", e);
        }
        return roles;
    }

    private SimpleGrantedAuthority getRole(String role) {
        LOG.debug(role);
        switch (role) {
            case "Domain users": return new SimpleGrantedAuthority(EnumRole.USER.toString());
            case "OIT": return new SimpleGrantedAuthority(EnumRole.SUPER_ADMIN.toString());
            case "ПСР Администратор": return new SimpleGrantedAuthority(EnumRole.PSR_ADMIN.toString());
            case "Анонимный": return new SimpleGrantedAuthority(EnumRole.ANONYMOUS.toString());
            default: return null;
        }
    }

}
