package ru.kolaer.server.webportal;

import javax.swing.*;
import java.awt.*;
import javax.naming.*;
import javax.naming.directory.*;
import javax.naming.ldap.*;
import java.util.*;
import java.security.*;

public class ADConnection {

    DirContext ldapContext;
    String baseName = ",cn=users,DC=kolaerlocal,DC=kolaer,DC=local";
    String serverIP = "aerdc01.kolaer.local";
    String modelUsername = "temp1";

    public ADConnection() {
        try {
            Hashtable ldapEnv = new Hashtable(11);
            ldapEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            ldapEnv.put(Context.PROVIDER_URL,  "ldap://" + serverIP + ":389");
            ldapEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
            ldapEnv.put(Context.SECURITY_PRINCIPAL, "danilovey");
            ldapEnv.put(Context.SECURITY_CREDENTIALS, "");
            //ldapEnv.put(Context.SECURITY_PROTOCOL, "ssl");
            ldapContext = new InitialDirContext(ldapEnv);
        }
        catch (Exception e) {
            System.out.println(" bind error: " + e);
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void update(String username) {
        try {
            System.out.println("updating...\n");
            ModificationItem[] mods = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("employeeID", "7777"));
            ldapContext.modifyAttributes("", mods);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void createNew(String username, String surname, String givenName) {
        try {
            String distinguishedName = "cn=" + username + baseName;
            Attributes newAttributes = new BasicAttributes(true);
            Attribute oc = new BasicAttribute("objectclass");
            oc.add("top");
            oc.add("person");
            oc.add("organizationalperson");
            oc.add("user");
            newAttributes.put(oc);
            newAttributes.put(new BasicAttribute("sAMAccountName", username));
            newAttributes.put(new BasicAttribute("userPrincipalName", username + "@" + serverIP));
            newAttributes.put(new BasicAttribute("cn", username));
            newAttributes.put(new BasicAttribute("sn", surname));
            newAttributes.put(new BasicAttribute("givenName", givenName));
            newAttributes.put(new BasicAttribute("displayName", givenName + " " + surname));
            System.out.println("Name: " + username + " Attributes: " + newAttributes);
            ldapContext.createSubcontext(distinguishedName, newAttributes);
        }
        catch (Exception e) {
            System.out.println("create error: " + e);
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void createClone(String username, String surname, String givenName) {
        try {
            Attributes modelAttributes = fetch(modelUsername);
            String distinguishedName = "cn=" + username + baseName;
            Attributes newAttributes = new BasicAttributes(true);
            newAttributes.put(modelAttributes.get("objectclass"));
            newAttributes.put(modelAttributes.get("userAccountControl"));
            newAttributes.put(new BasicAttribute("sAMAccountName", username));
            newAttributes.put(new BasicAttribute("userPrincipalName", username + "@" + serverIP));
            newAttributes.put(new BasicAttribute("cn", username));
            newAttributes.put(new BasicAttribute("sn", surname));
            newAttributes.put(new BasicAttribute("givenName", givenName));
            newAttributes.put(new BasicAttribute("displayName", givenName + " " + surname));
            System.out.println("distinguishedName: " + distinguishedName + " Attributes: " + newAttributes);
            ldapContext.createSubcontext(distinguishedName, newAttributes);
        }
        catch (Exception e) {
            System.out.println("create clone error: " + e);
            e.printStackTrace();
            System.exit(-1);
        }
    }


    public void updatePassword(String username, String password) {
        try {
            System.out.println("updating password...\n");
            String quotedPassword = "\"" + password + "\"";
            char unicodePwd[] = quotedPassword.toCharArray();
            byte pwdArray[] = new byte[unicodePwd.length * 2];
            for (int i=0; i<unicodePwd.length; i++) {
                pwdArray[i*2 + 1] = (byte) (unicodePwd[i] >>> 8);
                pwdArray[i*2 + 0] = (byte) (unicodePwd[i] & 0xff);
            }
            System.out.print("encoded password: ");
            for (int i=0; i<pwdArray.length; i++) {
                System.out.print(pwdArray[i] + " ");
            }
            System.out.println();
            ModificationItem[] mods = new ModificationItem[1];
            mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
                    new BasicAttribute("UnicodePwd", pwdArray));
            ldapContext.modifyAttributes("cn=" + username + baseName, mods);
        }
        catch (Exception e) {
            System.out.println("update password error: " + e);
            System.exit(-1);
        }
    }

    public Attributes fetch(String username) {
        Attributes attributes = null;
        try {
            System.out.println("fetching: " + username);
            DirContext o = (DirContext)ldapContext.lookup("cn=" + username + baseName);
            System.out.println("search done\n");
            attributes = o.getAttributes("");
            for (NamingEnumeration ae = attributes.getAll(); ae.hasMoreElements();) {
                Attribute attr = (Attribute)ae.next();
                String attrId = attr.getID();
                for (NamingEnumeration vals = attr.getAll(); vals.hasMore();) {
                    String thing = vals.next().toString();
                    System.out.println(attrId + ": " + thing);
                }
            }
        }
        catch (Exception e) {
            System.out.println(" fetch error: " + e);
            System.exit(-1);
        }
        return attributes;
    }

    public static void main(String[] args) {
        Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
        // the keystore that holds trusted root certificates
        //System.setProperty("javax.net.ssl.trustStore", "e:\\ldap\\keystore");
        //System.setProperty("javax.net.debug", "all");
        ADConnection adc = new ADConnection();
        adc.update("temp1");
        //adc.createClone("clone1", "Clone", "Clarissa");
        //adc.updatePassword("clone1", "xxxx");
        //adc.createNew("mytestuser", "User", "Joe");
         //Attributes a = adc.fetch("clone1");
    }

}