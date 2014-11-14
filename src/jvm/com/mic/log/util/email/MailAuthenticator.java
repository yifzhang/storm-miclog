package com.mic.log.util.email;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
 
public class MailAuthenticator extends Authenticator {
     
    /**
     * Represents the username of sending SMTP server.
     * <p>For example: If you use smtp.163.com as your smtp server, then the related
     * username should be: <br>'<b>testname@163.com</b>', or just '<b>testname</b>' is OK.
     */
    private String username = null;
    /**
     * Represents the password of sending SMTP sever.
     * More explicitly, the password is the password of username.
     */
    private String password = null;
 
    public MailAuthenticator(String user, String pass) {
        username = user;
    password = pass;
    }
 
    protected PasswordAuthentication getPasswordAuthentication() {
    return new PasswordAuthentication(username, password);
    }
}
