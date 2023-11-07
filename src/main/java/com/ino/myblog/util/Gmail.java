package com.ino.myblog.util;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;


public class Gmail extends Authenticator {
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication("hih1217@skuniv.ac.kr", "dlsgh2711!@#");
    }
}
