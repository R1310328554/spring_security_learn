package com.okta.spring.jwt.pkcs;

//***.middleground.msv.service.impl;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.pkcs.RSAPrivateKeyStructure;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;
import org.junit.Test;

import javax.crypto.Cipher;
import java.io.*;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author chenxiaolong
 * @description <p></p>
 * @company ***
 * @date 2019/9/17 12:44
 */
public class ConvertPkcs1{

    static KeyFactory keyFactory;

    static {
        try {
            keyFactory = KeyFactory.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testPkcs8ToPkcs1() throws IOException {
        String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIQeQqzER5h0HL60\n" +
                "TaBwjqQhtTl4gAOYnHNtLj5wvBCLCD6eiQeE5JavYZLZWAAv50fmZZO/FGfNqeG3\n" +
                "x+spaEPMB3c+NOv5cOetGxxZe1ErO7MwBxz/Vsx4E0+wGoSSukwp9NqrWfWJYq91\n" +
                "SMEJdylf5Q/CXfKbpv/frOuebTD7AgMBAAECgYB6vPIMNrycsSITOMX1Cxw49ue5\n" +
                "YGzs6lZOFVwkve65H7ClG/sJdTg3lO780dWjNt2SOkSboaR/Wt07yNR9b5ZgIpg9\n" +
                "Ab5EstjUz19aP0CyrZobklmBL74l4qm6nnIDqY5kw7+OJMqbEOs11xlNo65gkwuB\n" +
                "fKqp88+MxPgu86hBQQJBAPMCKgnaFagfbf8onXuTYm77tV0aQGEi0NrrjyitfiT+\n" +
                "s5ngByMEud+THIFbEwve0QzKQsS1i6ncvP7IzKwC9OMCQQCLLnE0k5cxFj2eUmPT\n" +
                "9CKLa+aPvwP4CRIyvnGY6jL3gRtjRkha3k60S9ZJL9eLv4Pc2dPEpCcbQlQmNudY\n" +
                "LCcJAkBW1jAlgxv4DvskkePss9cZ57KAiY/15hYSFNj8ZTrNh4KweuCx/89X+F7Y\n" +
                "Tq44sK+tTV2co288DNgwh5qz4P6XAkBxCD6X3GUlUFKqiW7Za6PCZtbQVuj6PyOw\n" +
                "YkoPTeQmbYu4jBOm+HQiqJRWy6vZqqeEbMM3J7k6whtlDaHEUddBAkEA2fgRHwEn\n" +
                "lJjD4jQ7AskL1XF8/4qM5g5I7VqyJTvRoW4n54wLNfM0Opjv2byvj1GU2qjWooBN\n" +
                "c+2XnS7835lsUw==";
        byte[] privBytes = Base64.decodeBase64(privateKey);
        PrivateKeyInfo pkInfo = PrivateKeyInfo.getInstance(privBytes);
        ASN1Encodable encodable = pkInfo.parsePrivateKey();
        AlgorithmIdentifier privateKeyAlgorithm = pkInfo.getPrivateKeyAlgorithm();
        System.out.println("privateKeyAlgorithm = " + privateKeyAlgorithm);
        System.out.println("privateKeyAlgorithm = " + privateKeyAlgorithm.getAlgorithm());
        System.out.println("privateKeyAlgorithm = " + privateKeyAlgorithm.getAlgorithm().toString());
        ASN1Primitive primitive = encodable.toASN1Primitive();
        byte[] privateKeyPKCS1 = primitive.getEncoded();
        String type = "RSA PRIVATE KEY";
        PemObject pemObject = new PemObject(type, privateKeyPKCS1);
        StringWriter stringWriter = new StringWriter();
        PemWriter pemWriter = new PemWriter(stringWriter);
        pemWriter.writeObject(pemObject);
        pemWriter.close();
        String pemString = stringWriter.toString();
        System.out.println("pemString = " + pemString);

        String[] split = pemString.split("-----");
        String replace = split[2].replace("\r\n", "");
        System.out.println(replace);


        toPem(pemString);


    }

    public static Key pem2Key(PemObject pemObject, String keyType) throws Exception {
        byte[] content = pemObject.getContent();

        Key key;
        if ("privateSSH".equalsIgnoreCase(keyType)) {
            // Exception in thread "main" java.security.spec.InvalidKeySpecException: Only RSAPrivate(Crt)KeySpec and PKCS8EncodedKeySpec supported for RSA private keys
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(content);
            String format = pkcs8EncodedKeySpec.getFormat();
            System.out.println("format = " + format);
            key = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
        } else if ("private".equalsIgnoreCase(keyType)) {
            // 取得私钥  for PKCS#1
            RSAPrivateKeyStructure asn1PrivKey = new RSAPrivateKeyStructure((ASN1Sequence) ASN1Sequence.fromByteArray(content));
            RSAPrivateKeySpec rsaPrivKeySpec = new RSAPrivateKeySpec(asn1PrivKey.getModulus(), asn1PrivKey.getPrivateExponent());
            PrivateKey priKey= keyFactory.generatePrivate(rsaPrivKeySpec);
            String format = priKey.getFormat();
            System.out.println("format = " + format);
            key = priKey;
        } else {

            X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(content);
            String format = x509KeySpec.getFormat();
            System.out.println("format = " + format);

            key = keyFactory.generatePublic(x509KeySpec);
        }
        return key;
    }

    public static String encryptByPem(PemObject pemObject, String keyType, String rawData) throws Exception {
        byte[] content = pemObject.getContent();
        System.out.println("ConvertPkcs1.encryptByPem");
//        System.out.println("content = \n" + new String(content)); // 乱码
        Key key = pem2Key(pemObject, keyType); // "public"
        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] outputBytes = cipher.doFinal(rawData.getBytes());
        return Base64.encodeBase64String(outputBytes);
    }

    public static String decryptByPem(PemObject pemObject, String keyType, String encrypted) throws Exception {
        byte[] content = pemObject.getContent();
        byte[] privateKeyBytes = Base64.decodeBase64(content);
        // 取得私钥  for PKCS#8
//        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        Key priKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 对数据解密
        Key key = pem2Key(pemObject, keyType); // "public"
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, key);

        byte[] inputBytes = Base64.decodeBase64(encrypted);
        byte[] outputBytes = cipher.doFinal(inputBytes);
        return new String(outputBytes);
//        return Base64.encodeBase64String(outputBytes);MTIzNDU2
    }

    public static PemObject toPem(String pemString) throws IOException {
        Reader rea = new StringReader(pemString);
        PemReader pr = new PemReader(rea);
        PemObject pemObject1 = pr.readPemObject();
        System.out.println("pemObject1 = " + pemObject1);

        String type1 = pemObject1.getType();
        System.out.println("type1 = " + type1);

//        PemObject generate = pemObject1.generate();
//        System.out.println("generate = " + generate);
        return pemObject1;
    }

}

