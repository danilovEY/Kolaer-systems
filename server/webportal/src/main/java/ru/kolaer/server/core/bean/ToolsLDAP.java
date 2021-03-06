package ru.kolaer.server.core.bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by danilovey on 30.08.2016.
 */
public class ToolsLDAP {
    private static final Logger LOG = LoggerFactory.getLogger(ToolsLDAP.class);
    private static final String DEFAULT_ROLE = "Domain users";

    public static Collection<? extends GrantedAuthority> getRolesFromAttributes(Attributes attributes) {
        LOG.debug("Attributes: {}", attributes);

        final List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(DEFAULT_ROLE));
        try {
            final Attribute cn = attributes.get("memberOf");
            if(cn != null) {
                for (int i = 0; i < cn.size(); i++) {
                    final String value = (String) cn.get(i);
                    final String group = value.substring(3, value.indexOf(','));
                    if (!group.equals(DEFAULT_ROLE))
                        roles.add(new SimpleGrantedAuthority(group));
                }
            }
        } catch (NamingException e) {
            LOG.error("Ошибка при парсинге атрибутов!", e);
        }

        return roles;
    }

    public static String toDC(String domainName) {
        StringBuilder buf = new StringBuilder();
        for (String token : domainName.split("\\.")) {
            if(token.length()==0) continue;   // defensive check
            if(buf.length()>0)  buf.append(",");
            buf.append("DC=").append(token);
        }
        return buf.toString();
    }

}
