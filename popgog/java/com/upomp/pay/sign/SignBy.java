package com.upomp.pay.sign;

import java.io.InputStream;



/**
 * 生成签名
 */

public class SignBy {


    public static String createSign(String  original_string,String alias,String password,InputStream PrivateSign){

        try {
            byte [] signsMD5 = Md5.MD5(original_string);

            byte [] signsRSA = RSA.rsaEncode(signsMD5,alias,password,PrivateSign);

            return new String(Base64.encode(signsRSA));

        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("Exception"+e);
        }

        return null;
    }




}
