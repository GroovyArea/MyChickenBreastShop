package com.daniel.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;

import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class PasswordEncrypt {

    Logger logger = LoggerFactory.getLogger(PasswordEncrypt.class);

    public static String getSalt() {
        Random random = new Random();
        byte[] salt = new byte[10];

        random.nextBytes(salt);

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < salt.length; i++) {
            sb.append(String.format("%02x", salt[i]));
        }

        return sb.toString();
    }

    public static String getSecurePassword(String pwd, String salt) throws NoSuchAlgorithmException {

        byte[] bytesArrOfSalt = salt.getBytes();
        String result;

        byte[] temp = pwd.getBytes();
        byte[] bytes = new byte[temp.length + bytesArrOfSalt.length];

        System.arraycopy(temp, 0, bytes, 0, temp.length);
        System.arraycopy(salt.getBytes(), 0, bytes, temp.length, salt.getBytes().length);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(bytes);

        byte[] b = md.digest();

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < b.length; i++) {
            sb.append(Integer.toString((b[i] & 0xFF) + 256, 16).substring(1));
        }

        result = sb.toString();

        return result;
    }

}
