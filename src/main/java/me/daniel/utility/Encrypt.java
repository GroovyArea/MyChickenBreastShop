package me.daniel.utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Encrypt {

    public static String ecrypt(String source) {
        String password = "";
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(source.getBytes());

            byte[] digest = md.digest();

            for (int i = 0; i < digest.length; i++) {
                password += Integer.toHexString(digest[i] & 0xff);
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("잘못된 암호화 알고리즘을 사용 하였습니다.");
        }
        return password;
    }
}
