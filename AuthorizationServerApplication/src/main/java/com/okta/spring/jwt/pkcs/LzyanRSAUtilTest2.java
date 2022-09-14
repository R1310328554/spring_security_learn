package com.okta.spring.jwt.pkcs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import sun.security.util.DerInputStream;
import sun.security.util.DerValue;

/**
 *
 * RSA+Base64加密解密工具类 RSA 一般用在数据传输过程中的加密和解密 先用RSA加密后是字节数组 再用BASE64加密 成字符串进行传输 测试
 * RSA1024产生的公钥字节长度在160-170之间 私钥长度在630-640之间 经过base64加密后长度 公钥字节产度在210-220之间
 * 私钥长度在840-850之间 所以数据库设计时如果存公钥长度设计varchar(256) 私钥长度varchar(1024)
 *
 */
public abstract class LzyanRSAUtilTest2 {
    public static final String KEY_ALGORITHM = "RSA";
    private static final String PUBLIC_KEY = "rsa_public_key";
    private static final String PRIVATE_KEY = "rsa_private_key";
    private static final String ENCODING = "UTF-8";

    /**
     *
     * 加密
     * 用公钥加密
     * @param content
     * @param base64PublicKeyStr
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String content, String base64PublicKeyStr) throws Exception {
        byte[] inputBytes = content.getBytes(ENCODING);
        byte[] outputBytes = encryptByPublicKey(inputBytes, base64PublicKeyStr);
        return Base64.encodeBase64String(outputBytes);
    }

    /**
     *
     * 加密
     *
     * 用私钥加密
     * @param content
     * @param base64PrivateKeyStr
     * @return
     * @throws Exception
     */
    public static String encryptByPrivateKey(String content, String base64PrivateKeyStr) throws Exception {
        byte[] inputBytes = content.getBytes(ENCODING);
        byte[] outputBytes = encryptByPrivateKey(inputBytes, base64PrivateKeyStr);
        return Base64.encodeBase64String(outputBytes);
    }

    /**
     *
     * 解密
     * 用公钥解密
     * @param content
     * @param base64PublicKeyStr
     * @return
     * @throws Exception
     */
    public static String decryptByPublicKey(String content, String base64PublicKeyStr) throws Exception {
        byte[] inputBytes = Base64.decodeBase64(content);
        byte[] outputBytes = decryptByPublicKey(inputBytes, base64PublicKeyStr);
        return new String(outputBytes, ENCODING);
    }

    /**
     *
     * 解密
     * 用私钥解密
     * @param content
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String content, String base64PrivateKeyStr) throws Exception {
        byte[] inputBytes = Base64.decodeBase64(content);
        byte[] outputBytes = decryptByPrivateKey(inputBytes, base64PrivateKeyStr);
        return new String(outputBytes, ENCODING);

    }

    /**
     *
     * 加密
     * 用公钥加密
     * @param content
     * @param base64PublicKeyStr
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] content, String base64PublicKeyStr) throws Exception {
        // 对公钥解密
        byte[] publicKeyBytes = Base64.decodeBase64(base64PublicKeyStr);
        return ByPublicKey(content, publicKeyBytes, Cipher.ENCRYPT_MODE);

    }

    public static byte[] ByPublicKey(byte[] content, byte[] publicKeyBytes, int encryptMode) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 取得公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(encryptMode, publicKey);

        return cipher.doFinal(content);
    }

    /**
     *
     * 加密
     * 用私钥加密
     * @param content
     * @param base64PrivateKeyStr
     * @return
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] content, String base64PrivateKeyStr) throws Exception {
        // 对密钥解密
        byte[] privateKeyBytes = Base64.decodeBase64(base64PrivateKeyStr);
        return ByPrivateKey(content, privateKeyBytes, Cipher.ENCRYPT_MODE);

    }

    public static byte[] ByPrivateKey(byte[] content, byte[] privateKeyBytes, int encryptMode) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 取得私钥
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(encryptMode, privateKey);

        return cipher.doFinal(content);
    }

    /**
     *
     * 解密
     * 用公钥解密
     * @param content
     * @param base64PublicKeyStr
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] content, String base64PublicKeyStr) throws Exception {
        // 对密钥解密
        byte[] publicKeyBytes = Base64.decodeBase64(base64PublicKeyStr);

        // 取得公钥
        // 对数据解密
        return ByPublicKey(content, publicKeyBytes, Cipher.DECRYPT_MODE);

    }

    /**
     * 解密
     * 用私钥解密
     * @param content
     * @param privateKeyStr
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] content, String base64PrivateKeyStr) throws Exception {
        // 对密钥解密
        byte[] privateKeyBytes = Base64.decodeBase64(base64PrivateKeyStr);

        // 取得私钥  for PKCS#1
//        RSAPrivateKeyStructure asn1PrivKey = new RSAPrivateKeyStructure((ASN1Sequence) ASN1Sequence.fromByteArray(privateKeyBytes));
//        RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent());
//        KeyFactory keyFactory= KeyFactory.getInstance("RSA");
//        PrivateKey priKey= keyFactory.generatePrivate(rsaPrivKeySpec);


        // 取得私钥  for PKCS#8
        return ByPrivateKey(content, privateKeyBytes, Cipher.DECRYPT_MODE);
    }

    /**
     *
     * 取得私钥
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getBase64PrivateKeyStr(Map keyMap) throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     *
     * 取得公钥
     *
     * @param keyMap
     * @return
     * @throws Exception
     */
    public static String getBase64PublicKeyStr(Map keyMap) throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return Base64.encodeBase64String(key.getEncoded());
    }

    /**
     *
     * 初始化密钥
     * @return
     * @throws Exception
     */
    public static Map initKey() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGen.initialize(1024); // 初始化RSA1024安全些
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic(); // 公钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate(); // 私钥
        Map keyMap = new HashMap(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    private static void test() throws Exception {
        RSA256Key rsa256Key = RsaUtil.generateRSA256Key();
        RSAPublicKey publicKey = rsa256Key.getPublicKey();
        RSAPrivateKey privateKey = rsa256Key.getPrivateKey();
        byte[] encoded = publicKey.getEncoded();
        System.out.println("publicKey = " + publicKey.toString());
        System.out.println("privateKey = " + privateKey.toString());

        String str = "123456123qwer111";
        byte[] outputBytes = ByPublicKey(str.getBytes(), encoded, Cipher.ENCRYPT_MODE);
        String encrypted = Base64.encodeBase64String(outputBytes);
        System.out.println("encrypted = " + encrypted);

        // priKey_pkcs1 不能解密
        // 不能使用 encrypted.getBytes()
        byte[] bytes = ByPrivateKey(outputBytes, privateKey.getEncoded(), Cipher.DECRYPT_MODE);
        String raw = Base64.encodeBase64String(bytes );
        System.out.println("raw = " + raw);// MTIzNDU2MTIzcXdlcg==
        raw = new String(bytes );
        System.out.println("raw = " + raw);

        if (!str.equalsIgnoreCase(raw)) {
            throw new RuntimeException("加密解密不匹配");
        }
     }

    private static void test2() throws Exception {
        String pubKey =
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgDxka1U8SWI1vBY7pA1UiCVnr" +
                "dtSgE+PpyTqe2YSEWCgkYQ2YsohZwsaUao7nya7QnBgRiPKEHgS/Eey+L9iwo32S" +
                "n5fUwb0nJ1+JeXRA6JsDEKpONJojIbF2nfgHLWsNn4bzn5Webc6WZLx0GyLTQuZG" +
                "adFVuVq2dQqEsrq7HwIDAQAB";

        String str = "123456123qwer";
        String content = encryptByPublicKey(str, pubKey);
        System.out.println(content);

        String priKey_pkcs1 = "MIICXQIBAAKBgQDgDxka1U8SWI1vBY7pA1UiCVnrdtSgE+PpyTqe2YSEWCgkYQ2YsohZwsaUao7nya7QnBgRiPKEHgS/Eey+L9iwo32Sn5fUwb0nJ1+JeXRA6JsDEKpONJojIbF2nfgHLWsNn4bzn5Webc6WZLx0GyLTQuZGadFVuVq2dQqEsrq7HwIDAQABAoGAYtaGLo4WWXNywJzlE+kCbwdNAU/kL9FWYtT/5P7zNCZnXtTpWIi5GU+QpfvzmlAfq6qP+3w77wgG8/qGQsd8gGu3mydi0ImmD9sJdhhsJuWZhCMM+qmvSmvG/gvIr+bdEmPhpCQpa3BLveUkFDA/OnwfTVL6ruwZayMzuToB6WECQQD63Gx9DZVhYoSxR7qSmiGf/TjfOJusTcrmc27Z5X5MS36a3Ux9Z+c9EaYFldZ3cPzP521ugNVvZdovKqFKIcQ5AkEA5KYeKBVlkrLaamSEu5WAX3DqJ6iDRqEjzMoVad5B1I7kJHO+NijUxNHaWaSqLOHuk37X+EAjSTozzwOkKwbqFwJBAM1NhhAWBNHtcdEwddWzBJ/N+jRdPLIX/Fz7zZXQRruj8VpGkGn1lf6ZqfjaNuoLcyunKB0OnR6NCbIePl/QIKkCQGgEQjfN9BVWlBJOhCuqCWphvcQo3v+kktq5HCC7YYtHLfZ/SQrubEzVgtXBGUGtzpD+5VUkKGlJtwP4Dhkc3iUCQQCwiFKuQe/OdlkYk1L4mb0H0fzy+/6mYxyUqpTXUw/6BVDOyowvzieh9oh2ZhnQS7YPBWz5ZXzwUH4YVwGqxiwA";
        String priKey_pkcs8_openssl = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAOAPGRrVTxJYjW8FjukDVSIJWet21KAT4+nJOp7ZhIRYKCRhDZiyiFnCxpRqjufJrtCcGBGI8oQeBL8R7L4v2LCjfZKfl9TBvScnX4l5dEDomwMQqk40miMhsXad+Actaw2fhvOflZ5tzpZkvHQbItNC5kZp0VW5WrZ1CoSyursfAgMBAAECgYBi1oYujhZZc3LAnOUT6QJvB00BT+Qv0VZi1P/k/vM0Jmde1OlYiLkZT5Cl+/OaUB+rqo/7fDvvCAbz+oZCx3yAa7ebJ2LQiaYP2wl2GGwm5ZmEIwz6qa9Ka8b+C8iv5t0SY+GkJClrcEu95SQUMD86fB9NUvqu7BlrIzO5OgHpYQJBAPrcbH0NlWFihLFHupKaIZ/9ON84m6xNyuZzbtnlfkxLfprdTH1n5z0RpgWV1ndw/M/nbW6A1W9l2i8qoUohxDkCQQDkph4oFWWSstpqZIS7lYBfcOonqINGoSPMyhVp3kHUjuQkc742KNTE0dpZpKos4e6Tftf4QCNJOjPPA6QrBuoXAkEAzU2GEBYE0e1x0TB11bMEn836NF08shf8XPvNldBGu6PxWkaQafWV/pmp+No26gtzK6coHQ6dHo0Jsh4+X9AgqQJAaARCN830FVaUEk6EK6oJamG9xCje/6SS2rkcILthi0ct9n9JCu5sTNWC1cEZQa3OkP7lVSQoaUm3A/gOGRzeJQJBALCIUq5B7852WRiTUviZvQfR/PL7/qZjHJSqlNdTD/oFUM7KjC/OJ6H2iHZmGdBLtg8FbPllfPBQfhhXAarGLAA=";
        String priKey_pkcs8_new = "MIICdwIBADANBgsqhkiG9w0BDAoBAgSCAmEwggJdAgEAAoGBAOAPGRrVTxJYjW8FjukDVSIJWet21KAT4+nJOp7ZhIRYKCRhDZiyiFnCxpRqjufJrtCcGBGI8oQeBL8R7L4v2LCjfZKfl9TBvScnX4l5dEDomwMQqk40miMhsXad+Actaw2fhvOflZ5tzpZkvHQbItNC5kZp0VW5WrZ1CoSyursfAgMBAAECgYBi1oYujhZZc3LAnOUT6QJvB00BT+Qv0VZi1P/k/vM0Jmde1OlYiLkZT5Cl+/OaUB+rqo/7fDvvCAbz+oZCx3yAa7ebJ2LQiaYP2wl2GGwm5ZmEIwz6qa9Ka8b+C8iv5t0SY+GkJClrcEu95SQUMD86fB9NUvqu7BlrIzO5OgHpYQJBAPrcbH0NlWFihLFHupKaIZ/9ON84m6xNyuZzbtnlfkxLfprdTH1n5z0RpgWV1ndw/M/nbW6A1W9l2i8qoUohxDkCQQDkph4oFWWSstpqZIS7lYBfcOonqINGoSPMyhVp3kHUjuQkc742KNTE0dpZpKos4e6Tftf4QCNJOjPPA6QrBuoXAkEAzU2GEBYE0e1x0TB11bMEn836NF08shf8XPvNldBGu6PxWkaQafWV/pmp+No26gtzK6coHQ6dHo0Jsh4+X9AgqQJAaARCN830FVaUEk6EK6oJamG9xCje/6SS2rkcILthi0ct9n9JCu5sTNWC1cEZQa3OkP7lVSQoaUm3A/gOGRzeJQJBALCIUq5B7852WRiTUviZvQfR/PL7/qZjHJSqlNdTD/oFUM7KjC/OJ6H2iHZmGdBLtg8FbPllfPBQfhhXAarGLAA=";
        String priKey = priKey_pkcs8_new;
        priKey = priKey_pkcs8_openssl;
        // priKey_pkcs1 不能解密

        String output = decryptByPrivateKey(content, priKey);
        if (!str.equalsIgnoreCase(output)) {
            throw new RuntimeException("加密解密不匹配");
        }
        System.out.println(output);

     }

    // === Testing ===
    public static void main(String[] args) throws Exception {
        test();
//        test3();
    }

    private static void test3() throws Exception{
        String keyBase64 = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCqwihm14wGhGI6Tb1FCsV4GYFYArOvgYID7eT+91+ppGZjz7khLLmWxoMQfG9pd57/L6Smyeo9LS1rt3Z/YFfaMVSiV0Ku4Mqp9HArzkMrrGK64DORMAmy+zvuj5nA0PimX05jo+gGjpSYKVDNKjas3Qu/OlxHeYSPmdAH7/vevQK2okg39qqGeSJwOxeVW9FvT9S4IlY740RXvqtDRIY1meWq/wNmwqkS1+pn952xhOOY3r4AcsUoaprYAmWGnNg0cCpjHw4Se/IhzEz4xKyUXwOJxMVayUz9B3aqNARwITogGzZxEODndztxt5gJmvY9k28qwSY8Id9b2AfWH60vAgMBAAECggEBAIfWKBmXgjZ0/UbwLI5NwUSG1ZPTomkNFwZVKoOA+cKTmzfvOJAhagl9JSnqMx6tH/hUSCC+Iu5DkAxOR5+QXpKeB3uU+uDuxYlpgAn47QpC+c87JwpqMm0E2mouJdty4TpIOq2c56i/p4lb5IExAYmF1iBrJpldG4y4iPqnd695Uiwa/CbFvvU835ANGVZQvUZXi+TUq2vim8/826GSs7oleOn2WroMBULKXat0H61fSiSsoYMRrPyoKBV/wss7oXpL6R1ym1hdn2oeJlfak19GPG4/40YS88PAOiJXs9iT7JzHRZHwUFKt65762RnUqWxKGUOLWr37X+AgLSrv8NECgYEA5VHMsuHuFjpQM6jeORW8HkmkkxzvCjNfOW/I0oGVc6QAQYEMP1OsUWLSHQqaPEVN2PQkCxS41cBbWYXHWzI14/y+QJjXIjJlpFBP03BEgaArvO1JUzLpQZfxmEDW9Wkrx7dN8OqeOf00ml/FkMVjFWBwWo/PzcNKC6D8fbhmFasCgYEAvqAi//POUEN/rSQ5tqiB9FepsY/iTaGfZSggX3KGtljKXfof1xh3JmcQwAxsIDwCcYliXoqkM408VsqQ86D7qGGfiBDp8RrmQGyHspyoiQdNw5vNkfc2NqHx++aZiNf1E+PeEELDXkZ1CcGCqNr4LJj2i46dU6xDsy+jErf3Oo0CgYAWFS14IGeT/mOQxfc4Wg52gDL387ZVLiNCXbBiDRZ+P1HC/RFX28/hOnnvUAEQQsA/XytFYeZ2twJU+Zv7/TbRUJplkBJPebjt/MnjG5GNRgZQOC1ydJmftmkOd1f74/fSUKWRyJYaEXcKUqSsz0U+Xr4TCnxBXUf5HLSTyrkR5wKBgHxUxB8+7KzLKaZLP11UkNZGqwaF+yt5JBZ9UogOkbCzPuKGtme7rFMe2/z23Ts1CL72xXM2aBy7nMRbAKn2sM1FRyEfLwaz7cPNRcCIjyA45E6V+0GeBP0jKUqMQE3R1SZw9DCU6wo9WC7RyR/LGh00c3HYxeKCM/HWOz2FQbotAoGADl5jtbHiw1UqLHlxbsoj5K39OdSXNe8sOuHuchCkG54ImOBeOxWl1KgJfMOV3FvuKjv/resTF8EJWeGkCSVYs6o7FURYnyXVWcBPPmvIDcDO1jvRk2K9EO5srRLhph7N4Mdn3Ae+ApU+QN1dZuxuZDxUrjwt6lOy+V1DUvDpUKw=";
        String pkcsPUBLICKeyStr = "-----BEGIN PUBLIC KEY-----\n" +
                "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnlZJeOzw0qYnM+HfBnSqhT9RA\n" +
                "8c8NR1qqy5JlwzZR1cP2x/XsHOsO7QI9m+Bui+gnVDNzSICXyJY/Sy7Y6mkvbcfQ\n" +
                "UK/eNRMl4fjsyUU4OngN0luw9AXKe56BqIA4xvKiIdbx95QuAmL/7sjvDLu4qaSm\n" +
                "OJRR58OYoJargfDCaQIDAQAB\n" +
                "-----END PUBLIC KEY-----\n";
        PemObject pemObject = ConvertPkcs1.toPem(pkcsPUBLICKeyStr);
        System.out.println("pemObject getHeaders = " + pemObject.getHeaders());

        String pkcsPrivateKeyStr = "-----BEGIN RSA PRIVATE KEY-----\n" +
                "MIICXgIBAAKBgQCnlZJeOzw0qYnM+HfBnSqhT9RA8c8NR1qqy5JlwzZR1cP2x/Xs\n" +
                "HOsO7QI9m+Bui+gnVDNzSICXyJY/Sy7Y6mkvbcfQUK/eNRMl4fjsyUU4OngN0luw\n" +
                "9AXKe56BqIA4xvKiIdbx95QuAmL/7sjvDLu4qaSmOJRR58OYoJargfDCaQIDAQAB\n" +
                "AoGBAIblnWaA5Bu4BRabQzr02mXzLnYAr5yOvWsSZwvHMah6HD5AA7NGK+hd3Bnf\n" +
                "qa8Sq5NM8gVwbSLA8kCMsm8lcfjPYSZ60B1MMbjccGx+X7ph6gr/WmrTOv9Bg5pL\n" +
                "FGuWTxraSmLtIlzc3mlLrj8wNkMItneFTFN6u25pb3bnImWhAkEA3pTI5HLQAxWi\n" +
                "2EqZg3ezjzolKIQxvP9OUZVwx6bbsDtT2TkMRVG1ZkpBWQlQT6pdxlTaYNhpV10+\n" +
                "enovISRbhQJBAMC+5338oVttcH1ua5S08zwfUpdlBUSapUsWgkXv9GIQyzv28EmK\n" +
                "pAQh9EFpHITE8CwL7NKoqKiNXMZbr5m25pUCQQCPQSta/RosIgzBMKJFHxGOKVop\n" +
                "DYwFnDwH/iqsFRRoBIce07y2ELSztS8B4yCxZXEdaRd0JVUHSZggs+sJZaERAkAH\n" +
                "f+YV+tx0aD+2QbOh56CHOFjIVz/rOXExCMEs43dI0Yp23NbgpapYFtIgMWzQpbEK\n" +
                "pbQjRji5xjZT6YFdo8Y5AkEAsIIeHU6YySYXOYrs6Uwx5vBGPf1/zCTPXR2B+XPW\n" +
                "KVaoJftvHvuG6s/xUV0N7OWqGMf/Ij5Ef/7Xo8ME29QvXA==\n" +
                "-----END RSA PRIVATE KEY-----\n";

        PemObject pemObjectPrivate = ConvertPkcs1.toPem(pkcsPrivateKeyStr);
        System.out.println("pemObject getHeaders = " + pemObjectPrivate.getHeaders());

//        PemReader reader = new PemReader(new InputStreamReader(new ByteArrayInputStream(keyBytes)));
//        PrivateKey key = (PrivateKey)reader.readObject();

        String rawData = "123456abc";
        String aPublic = ConvertPkcs1.encryptByPem(pemObject, "public", rawData);
        System.out.println("aPublic = " + aPublic);
        String aprivate = ConvertPkcs1.decryptByPem(pemObjectPrivate, "private", aPublic);
        System.out.println("aprivate = " + aprivate);
        System.out.println(aprivate + "   = " + aprivate.equalsIgnoreCase(rawData));

        /*
        可以公钥加密， 私钥解密， 也可以反之； 两种方式都可行

        String aPublic = ConvertPkcs1.encryptByPem(pemObjectPrivate, "private", rawData);
        System.out.println("aPublic = " + aPublic);
        String aprivate = ConvertPkcs1.decryptByPem(pemObject, "public", aPublic);
         System.out.println("aprivate = " + aprivate);
         */

        String s = Base64.encodeBase64String(rawData.getBytes());
        System.out.println("MTIzNDU2YWJj".equalsIgnoreCase(s) );

        // MTIzNDU2
//        rsa(keyFactory, keyBase64);

    }

    private static void rsa(KeyFactory keyFactory, String keyBase64) throws IOException, InvalidKeySpecException {
        DerInputStream derReader = new DerInputStream(java.util.Base64.getDecoder().decode(keyBase64));
        DerValue[] seq = derReader.getSequence(0);
        BigInteger modulus = seq[1].getBigInteger();
        BigInteger publicExp = seq[2].getBigInteger();
        BigInteger privateExp = seq[3].getBigInteger();
        BigInteger prime1 = seq[4].getBigInteger();
        BigInteger prime2 = seq[5].getBigInteger();
        BigInteger exp1 = seq[6].getBigInteger();
        BigInteger exp2 = seq[7].getBigInteger();
        BigInteger crtCoef = seq[8].getBigInteger();
        RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2, exp1, exp2, crtCoef);
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        System.out.println("privateKey = " + privateKey);
    }

}



