package com.example.encryptfolder.ui.Database;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class SaltedHash {
    private static final String pepper = "1Hbsfhfgkg67a48dkfDEJ78";

    public static String hashPassword(String password) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.reset();
        md.update(password.getBytes());
        byte[] mdArray = md.digest();
        StringBuilder sb = new StringBuilder(mdArray.length * 2);
        for(byte b : mdArray) {
            int v = b & 0xff;
            if(v < 16)
                sb.append('0');
            sb.append(Integer.toHexString(v));
        }
        return sb.toString();

    }
    public static String getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[32];
        sr.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);

    }
    public static String getPepper(){
        return pepper;
    }
}
