package org.qbix.pm.server.util;

import org.qbix.pm.server.model.UserAccount;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created with IntelliJ IDEA.
 * User: Roman
 * Date: 26.09.13
 * Time: 2:07
 * To change this template use File | Settings | File Templates.
 */
public class MD5Hashing {

    public static String getHash(String password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Hashing algorithm doesn't work.");
        }
        md.update(password.getBytes());
        byte byteData[] = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }

    public static String getActivationHash(UserAccount userAccount) {
        StringBuilder builder = new StringBuilder();
        builder.append(userAccount.getEmail());
        builder.append(userAccount.getEmail().length());
        builder.append(userAccount.getPassword());
        builder.append(userAccount.getPassword().length());
        builder.append(userAccount.getCreationDate().getTime());
        String hash = getHash(builder.toString());
        builder.append(builder.length() * hash.length());
        builder.append(hash);

        hash = getHash(builder.substring(builder.length()/2));
        return hash.substring(hash.length()/3);
    }
}
