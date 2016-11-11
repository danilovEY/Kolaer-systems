package ru.kolaer.server.webportal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.kolaer.api.mvp.model.kolaerweb.Counter;
import ru.kolaer.api.mvp.model.kolaerweb.CounterBase;
import ru.kolaer.server.webportal.config.SpringContext;
import ru.kolaer.server.webportal.config.SpringSecurityConfig;
import ru.kolaer.server.webportal.mvc.model.dao.CounterDao;
import ru.kolaer.server.webportal.mvc.model.ldap.RoleLDAP;
import ru.kolaer.server.webportal.mvc.model.servirces.CounterService;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import java.util.Date;

import static javax.naming.directory.SearchControls.SUBTREE_SCOPE;

/**
 * Created by danilovey on 10.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringContext.class, SpringSecurityConfig.class})
@WebAppConfiguration
public class TestSpring {

    @Autowired
    private InitialLdapContext ldapContext;

    @Test
    public void testJDBCController() {
        final SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(new String[]{
                "cn", "memberOf"
        });

        try {
            final NamingEnumeration<SearchResult> answer = this.ldapContext.search("", "(objectclass=group)", controls);
            while (answer.hasMoreElements()) {
                SearchResult rslt = (SearchResult) answer.next();
                Attributes attrs = rslt.getAttributes();
                System.out.println(attrs.get("cn").get());
            }

            answer.close();
            System.out.println("AAAAAAAA");
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

}
