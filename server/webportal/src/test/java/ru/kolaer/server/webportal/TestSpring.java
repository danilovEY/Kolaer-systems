package ru.kolaer.server.webportal;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.kolaer.server.webportal.config.SpringContext;
import ru.kolaer.server.webportal.config.SpringSecurityConfig;
import ru.kolaer.server.webportal.mvc.model.servirces.JournalViolationService;
import ru.kolaer.server.webportal.mvc.model.servirces.ViolationService;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import javax.naming.ldap.InitialLdapContext;

/**
 * Created by danilovey on 10.11.2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringContext.class, SpringSecurityConfig.class})
@WebAppConfiguration
public class TestSpring {

    @Autowired
    private InitialLdapContext ldapContext;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SimpleMailMessage templateMessage;


    @Autowired
    private ViolationService violationService;

    @Test
    @Ignore
    public void testMailSend() {
        SimpleMailMessage mailMessage = new SimpleMailMessage(templateMessage);
        mailMessage.setSubject("Hello TEST!");
        mailMessage.setText("TEST TEST!");
        mailMessage.setTo("danilovey@kolaer.ru");

        try {
            mailSender.send(mailMessage);
            System.out.println("Mail send!");
        } catch (Exception ex) {
            System.out.println("Mail NOT send!");
            ex.printStackTrace();
        }

    }

    @Test
    @Ignore
    public void testJDBCController() {
        final SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        controls.setReturningAttributes(new String[]{
                "cn", "memberOf", "employeeID", "distinguishedName"
        });

        try {
            NamingEnumeration<SearchResult> answer = this.ldapContext.search("", "(& (userPrincipalName=temp1@kolaer.local)(objectClass=person))", controls);
            SearchResult rslt = null;
            while (answer.hasMoreElements()) {
                rslt = (SearchResult) answer.next();
                Attributes attrs = rslt.getAttributes();
                System.out.println(attrs);
            }

            answer.close();

            Attributes attributes = rslt.getAttributes();
            attributes.get("employeeID").add("7777");
            ldapContext.modifyAttributes("CN=temp1", DirContext.REPLACE_ATTRIBUTE, attributes);

            /*Attribute attribute = new BasicAttribute("employeeID",
                    "7777");
            // array of modified iteams
            ModificationItem[] item = new ModificationItem[1];
            // replacing the value
            item[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attribute);
            // changing the value of the attribute


            answer = this.ldapContext.search("", "(& (userPrincipalName=temp1@kolaer.local)(objectClass=person))", controls);
            while (answer.hasMoreElements()) {
                rslt = (SearchResult) answer.next();
                Attributes attrs = rslt.getAttributes();
                System.out.println("-------======: " + attrs);
            }*/

        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

}
