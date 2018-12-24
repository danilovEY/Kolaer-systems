package ru.kolaer.server.webportal;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Hashtable;

public class Setpass {
    public static void main(String[] args) {

        Hashtable env = new Hashtable();
        String adminName = "admindc";
        String adminPassword = "2Serdce3";
        String userName = "CN=temp1, DC=kolaer,DC=local";
        String searchBase = "DC=kolaer,DC=local";


        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");

        //set security credentials, note using simple cleartext authentication

        env.put(Context.SECURITY_PRINCIPAL, adminName);
        env.put(Context.SECURITY_CREDENTIALS, adminPassword);

        //env.put(Context.SECURITY_AUTHENTICATION,"DIGEST-MD5"); //No other SALS worked with me

        //connect to my domain controller
        String ldapURL = "LDAP://aerdc01.kolaer.local:389";
        env.put(Context.PROVIDER_URL, ldapURL);

        try {

            // Create the initial directory context
            DirContext ctx = new InitialDirContext(env);

            final SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            controls.setReturningAttributes(new String[]{
                    "cn", "memberOf", "employeeID", "distinguishedName"
            });

            NamingEnumeration<SearchResult> answer = ctx.search("DC=kolaer,DC=local", "(& (userPrincipalName=temp1@kolaer.local)(objectClass=person))", controls);
            SearchResult rslt = null;
            while (answer.hasMoreElements()) {
                rslt = (SearchResult) answer.next();
                Attributes attrs = rslt.getAttributes();
                System.out.println(attrs);
            }

            answer.close();

            Attributes attributes = rslt.getAttributes();
            attributes.get("employeeID").set(0, "7777");
            System.out.println(attributes);
            System.out.println(rslt.getName());

            ModificationItem[] mods = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("employeeID", "7777"));
            //ldapContext.modifyAttributes("", mods);

            ctx.modifyAttributes(rslt.getName()+",DC=kolaer,DC=local", mods);

            ctx.close();

        } catch (NamingException e) {
            e.printStackTrace();
        }

    }
}