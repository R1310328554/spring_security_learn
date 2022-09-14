package com.okta.spring.jwt.pkcs;

import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.stereotype.Component;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;


/**
 * auth0 方式
 */
@Component
public class RsaUtil {

    public static void main(String[] args) {

        try {
            RSA256Key rsa256Key = generateRSA256Key();
            RSAPrivateKey privateKey = rsa256Key.getPrivateKey();
            System.out.println("privateKey = " + privateKey.getFormat());
            System.out.println("privateKey = " + privateKey.getAlgorithm());
            System.out.println("privateKey = " + privateKey.getPrivateExponent());
            System.out.println("privateKey = " + privateKey.getModulus());
            System.out.println("privateKey = " + privateKey.isDestroyed());
            System.out.println("privateKey = " + privateKey.toString());

            RSAPublicKey publicKey = rsa256Key.getPublicKey();
            System.out.println("publicKey = " + publicKey.getFormat());
            System.out.println("publicKey = " + publicKey.getAlgorithm());
            System.out.println("publicKey = " + publicKey.getPublicExponent());
            System.out.println("publicKey = " + publicKey.getModulus());
            System.out.println("publicKey = " + publicKey.toString());
//            System.out.println("publicKey = " + publicKey);

            String algorithm = publicKey.getAlgorithm();
            System.out.println("algorithm = " + algorithm);

            String s = StringUtils.newStringUtf8(Base64.encodeBase64(publicKey.getEncoded()));
            System.out.println("publicKey = " + s);
              s = StringUtils.newStringUtf8(Base64.encodeBase64(privateKey.getEncoded()));
            System.out.println("privateKey = " + s);

            Map<String, Object> stringObjectMap = generateJWK(publicKey);


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static Map<String, Object> generateJWK(PublicKey publicKey){
        RSAPublicKey rsa = (RSAPublicKey) publicKey;
        Map<String, Object> values = new HashMap<>();
        values.put("kty", rsa.getAlgorithm()); // getAlgorithm() returns kty not algorithm
        values.put("kid", "someuniqueid");
        values.put("alg", "RS256");
        values.put("use", "sig");

        String s = java.util.Base64.getUrlEncoder().encodeToString(rsa.getModulus().toByteArray());
        System.out.println("s = " + s);

        String s1 = java.util.Base64.getUrlEncoder().encodeToString(rsa.getPublicExponent().toByteArray());
        System.out.println("s1 = " + s1);

        values.put("n", s);
        values.put("e", s1);

        return values;
    }


    /**
     *
     * 签发Token
     * <p>
     * withIssuer()给PAYLOAD添加一跳数据 => token发布者
     * withClaim()给PAYLOAD添加一跳数据 => 自定义声明 （key，value）
     * withIssuedAt() 给PAYLOAD添加一条数据 => 生成时间
     * withExpiresAt()给PAYLOAD添加一条数据 => 保质期
     *
     * @param data
     * @return
     * @throws NoSuchAlgorithmException
     */
//    public static String creatTokenByRS256(Object data) throws NoSuchAlgorithmException {
//        //初始化 公钥/私钥
//        RSA256Key rsa256Key = SecretKeyUtil.generateRSA256Key();
//
//        //加密时，使用私钥生成RS算法对象
//        Algorithm algorithm = Algorithm.RSA256(rsa256Key.getPrivateKey());
//
//        return JWT.create()
//                //签发人
//                .withIssuer(ISSUER)
//                //接收者
//                .withAudience(data.toString())
//                //签发时间
//                .withIssuedAt(new Date())
//                //过期时间
//                .withExpiresAt(DateUtil.addHours(2))
//                //相关信息
//                .withClaim("data", JsonUtil.toJsonString(data))
//                //签入
//                .sign(algorithm);
//    }

    //数字签名
    public static final String KEY_ALGORITHM = "RSA";

    //RSA密钥长度
     public static final int KEY_SIZE = 1024;// jwt 需要至少2048
//    public static final int KEY_SIZE = 2048;//
//    public static final int KEY_SIZE = 4096;//

    //唯一的密钥实例
    private static volatile RSA256Key rsa256Key;

    /**
     * 生成 公钥/私钥
     * <p>
     * 由双重校验锁保证创建唯一的密钥实例，因此创建完成后仅有唯一实例。
     * 当被JVM回收后，才会创建新的实例
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static RSA256Key generateRSA256Key() throws NoSuchAlgorithmException {
        //第一次校验：单例模式只需要创建一次实例，若存在实例，不需要继续竞争锁，
        if (rsa256Key == null) {
            //RSA256Key单例的双重校验锁
            synchronized (RSA256Key.class) {
                //第二次校验：防止锁竞争中自旋的线程，拿到系统资源时，重复创建实例
                if (rsa256Key == null) {
                    //密钥生成所需的随机数源
                    KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
                    keyPairGen.initialize(KEY_SIZE);
                    //通过KeyPairGenerator生成密匙对KeyPair
                    KeyPair keyPair = keyPairGen.generateKeyPair();
                    //获取公钥和私钥
                    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
                    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
                    rsa256Key = new RSA256Key();
                    rsa256Key.setPublicKey(publicKey);
                    rsa256Key.setPrivateKey(privateKey);
                }

            }
        }
        return rsa256Key;
    }

}


@Data
class RSA256Key {

    private RSAPublicKey publicKey;
    private RSAPrivateKey privateKey;

    public RSA256Key() {
    }

    public RSA256Key(RSAPublicKey publicKey, RSAPrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
}
//   **省略getter和setter***

