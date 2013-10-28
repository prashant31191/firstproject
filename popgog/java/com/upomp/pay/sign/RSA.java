package com.upomp.pay.sign;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;

public class RSA {


    /**
     *
     * @param signsRSA
     * @param isTest
     * @return
     * @throws IOException
     * @throws CertificateException
     * @throws NoSuchAlgorithmException
     */
    static byte[] rsaEncode(byte[] signsRSA,String alias,String pwd,InputStream dataSign) {


        // 以PKCS2类型打开密钥库
        try {

            KeyStore store = KeyStore.getInstance("PKCS12");
            InputStream inStream = dataSign;
            store.load(inStream, pwd.toCharArray());
            inStream.close();
            // 从密钥库中提取密钥
            PrivateKey pKey = (PrivateKey)store.getKey(alias, pwd.toCharArray());
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pKey);
            return cipher.doFinal(signsRSA);





        } catch (Exception e) {

            e.printStackTrace();
        }



        return null;
    }


}
