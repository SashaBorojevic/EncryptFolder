package com.example.encryptfolder.ui.Database;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.security.Key;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AESCrypt {
    private static final String ALGORITHM = "AES";

    public static String Encrypt(String value) throws Exception
    {
        SecretKey key = generateKey();
        char[] password = "tcejorPlaniF694TPMC".toCharArray();
        KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
        ks.load(null);
        KeyStore.ProtectionParameter protectionParam = new KeyStore.PasswordProtection(password);
        KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(key);
        //Set the entry to the keystore
        ks.setEntry("secretKeyAlias", secretKeyEntry, protectionParam);

        Cipher cipher = Cipher.getInstance(AESCrypt.ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte [] encryptedByteValue = cipher.doFinal(value.getBytes("utf-8"));
        String encryptedValue64 = Base64.encodeToString(encryptedByteValue, Base64.DEFAULT);
        return encryptedValue64;

    }

    public static String Decrypt(String value) throws Exception
    {
        //retrive a key
        KeyStore ks = KeyStore.getInstance("AndroidKeyStore");
        ks.load(null);
        KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry)ks.getEntry("key1", null);
        SecretKey key = entry.getSecretKey();

        Cipher cipher = Cipher.getInstance(AESCrypt.ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedValue64 = Base64.decode(value, Base64.DEFAULT);
        byte [] decryptedByteValue = cipher.doFinal(decryptedValue64);
        String decryptedValue = new String(decryptedByteValue,"utf-8");
        return decryptedValue;

    }

    private static SecretKey generateKey() throws Exception {
        KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder("key1",
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT);
        KeyGenParameterSpec keySpec = builder
                .setKeySize(256)
                .setRandomizedEncryptionRequired(true)
                .build();
        KeyGenerator kg = KeyGenerator.getInstance("AES", "AndroidKeyStore");
        kg.init(keySpec);
        SecretKey key = kg.generateKey();
        return key;
    }
}
