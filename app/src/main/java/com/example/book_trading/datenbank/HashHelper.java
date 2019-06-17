package com.example.book_trading.datenbank;

import android.util.Base64;
import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class HashHelper {
    private static String AES = "AES";

    public static String encrypt(String password) throws Exception{  //Verschlüsseln
        SecretKeySpec key = generateKey (password);
        Cipher c = Cipher.getInstance(AES);
        c.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = c.doFinal(password.getBytes());
        String encryptedValue = Base64.encodeToString(encVal, Base64.DEFAULT);
        //Modifiziertes Passwort letze 3 Stellen werden abgeschnitten weil sonst probleme mit der datenbank
        int tmp = encryptedValue.length();
        String modencryptedValue = (String) encryptedValue.subSequence(0,tmp-3);
        return modencryptedValue;
    }

    private static SecretKeySpec generateKey(String password) throws Exception{    //Schlüssel generieren
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }

}