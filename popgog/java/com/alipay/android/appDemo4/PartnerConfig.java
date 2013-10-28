package com.alipay.android.appDemo4 ;
public class PartnerConfig {
    // 合作商户ID。用签约支付宝账号登录ms.alipay.com后，在账户信息页面获取。
    public static final String PARTNER = "2088801270797940";

    // 商户收款的支付宝账号
    public static final String SELLER = "2088801270797940";
    // 商户（RSA）私钥
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMiZDZ0EOptItU0JLhdJO4vFDRrI4xhTO1+p/NTvVrQD8jvrY9weWBNxeKatUb/Ui0biChJWwpGtYbTCPSqM8zSdV4ysYe1I+EFovjoJ8RPEHJ5ftVj8bMansAdYMgLTNFIkjpwsX8M8JOetlr1NLcOH2mG0obiBECvbQWCWFn0zAgMBAAECgYAho3ji8ciaLbEpf/aYtRElOSAfc7yRYbO9MRZS5ufVPCws3CZczUHb0NV4revdjVjlxBYISUBLiZWu6Du1nPlTdvd5FPyiup4NNaSggz3uEIZAQTbpWWj5umiIvATeuXsg/w7ozAoXhRn3ohLX3zxx9p71azsK2AA8ejADNGo7cQJBAPwjEVqz5rj+GE37WLCHN6X2cQULkxs3dLCgWeK2rGoNKOz0d014F0d4m2L5G662+reuzdTCbnIWR/irDXOctl0CQQDLq9aqwyLzqCuIwkVgWEQLadnhmdOphz3afy6iTvf4FnixDwlExZRmr3LpbCgF4aztx1Ha7FUHZ1xCFK9eWqjPAkAX21WUCHunO1ufXU3p/hFU4bmV6Z90rs2avMFkgzZTHoWlzgfPy7IgMBg7yChHYsbcRGzzWaZLozISJFU0gDp9AkEAiWrlqc2DdZaP2PQKzxh+wJyorziUwRtItnV9R3G0iSTQRdZ5UHdmy2mxRKD+2AkOHliPeABg1l/8tXEzaZwNrQJAOsEHX9AyB5dNxLrV6316Cc9N48NfijslCtxKcWkd7UExi3gqxXAc2AUxRGEb98tcWDjGvlFsYNI5zhsgV5BeHA==";
    // 支付宝（RSA）公钥 用签约支付宝账号登录ms.alipay.com后，在密钥管理页面获取。
    public static final String RSA_ALIPAY_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCxw58EdUePy9pz6d8izwxEc4racjRuPqfxRrzMi+5JSFVr57950IOMNROcxLthvrN76fNZ6ihfrpRIJaHu44B6yyCVHMprkAtHTSTDHL1IULjCnOzDCz0oFFIi5j/EB7NVFKFCo5/kduhrhWfB9i9/6NRRpaUfPUBN/Z3dbNk/qQIDAQAB";
    // 支付宝安全支付服务apk的名称，必须与assets目录下的apk名称一致
    public static final String ALIPAY_PLUGIN_NAME = "alipay_plugin_20120428msp.apk";
}
