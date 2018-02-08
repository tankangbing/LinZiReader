package com.hn.epub;


import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateCrtKeySpec;
import javax.crypto.Cipher;
public class RsaHelper
{ 
    /*
     * 私钥转换
     */
    public static PrivateKey decodePrivateKeyFromXml(String xml)
    {
        xml = xml.replaceAll("\r", "").replaceAll("\n", "");
        BigInteger modulus =
            new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml,
                "<Modulus>", "</Modulus>")));
        BigInteger publicExponent =
            new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml,
                "<Exponent>", "</Exponent>")));
        BigInteger privateExponent =
            new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml, "<D>",
                "</D>")));
        BigInteger primeP =
            new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml, "<P>",
                "</P>")));
        BigInteger primeQ =
            new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml, "<Q>",
                "</Q>")));
        BigInteger primeExponentP =
            new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml,
                "<DP>", "</DP>")));
        BigInteger primeExponentQ =
            new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml,
                "<DQ>", "</DQ>")));
        BigInteger crtCoefficient =
            new BigInteger(1, Base64Helper.decode(StringUtils.getMiddleString(xml,
                "<InverseQ>", "</InverseQ>")));

        RSAPrivateCrtKeySpec rsaPriKey =
            new RSAPrivateCrtKeySpec(modulus, publicExponent, privateExponent, primeP,
                primeQ, primeExponentP, primeExponentQ, crtCoefficient);

        KeyFactory keyf;
        try
        {
            keyf = KeyFactory.getInstance("RSA");
            return keyf.generatePrivate(rsaPriKey);
        }
        catch (Exception e)
        {
            return null;
        }
    }

   
    // 用私钥解密
    public static byte[] decryptData(byte[] encryptedData, PrivateKey priKey)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            return cipher.doFinal(encryptedData);
        }
        catch (Exception e)
        {
            return null;
        }
    }

   
}
