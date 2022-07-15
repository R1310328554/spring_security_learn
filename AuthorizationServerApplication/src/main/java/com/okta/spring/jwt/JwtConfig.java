package com.okta.spring.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;


/*
    TokenStore 有4个实现： jdbc/redis/jwt/inMemory

    jwt即JwtTokenStore，其实是不会在服务端存储token的，这样的做法适合分布式部署；


    但是 如果是移动端，不支持cookie怎么办？
 */
@Configuration
public class JwtConfig {

    //公钥
    private static final String PUBLIC_KEY = "id_rsa.pub"; // "public.key";
    private static final String KEY_PASSWORD = "123456";

    /***
     * 定义JwtTokenStore
     * @param jwtAccessTokenConverter
     * @return
     */
    @Bean // 如果添加了JwtTokenStore， 那么之前的请求方式就不行了，为什么？
//    因为.. jwt的getAccessToken 一直返回null； 因为JwtTokenStore不会存储任何东西；需要请求者把token传过来
    // 401 {"error":"invalid_token","error_description":"Cannot convert access token to JSON"}
    // Cannot deserialize instance of `java.lang.String` out of START_OBJECT token
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        //A TokenStore implementation that just reads data from the tokens themselves. Not really a store since it never persists anything, and methods like getAccessToken(OAuth2Authentication) always return null. But nevertheless a useful tool since it translates access tokens to and from authentications. Use this wherever a TokenStore is needed, but remember to use the same JwtAccessTokenConverter instance (or one with the same verifier) as was used when the tokens were minted.
        return new JwtTokenStore(jwtAccessTokenConverter);
    }

    /***
     * 定义JJwtAccessTokenConverter 转化器 既能创建令牌 也能解析令牌 用来配置公钥
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        // Caused by: java.lang.IllegalStateException: For MAC signing you do not need to specify the verifier key separately, and if you do it must match the signing key
        // converter.setVerifierKey(getPubKey()); // setVerifierKey需要的是一个字符串信息 getPubKey()得到文件中的公钥
        return converter;
    }
    /**
     * 获取非对称加密公钥 Key
     * @return 公钥 Key
     */
    private String getPubKey() {
        Resource resource = new ClassPathResource(PUBLIC_KEY);
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(resource.getInputStream());
            BufferedReader br = new BufferedReader(inputStreamReader);
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

//    版权声明：本文为CSDN博主「YxinMiracle」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
//    原文链接：https://blog.csdn.net/caiyongxin_001/article/details/119790949




    /**
     * Jwt 访问令牌转换器,可以在此方法中设置密钥, 或者手动写死密钥值。
     * 这边根据非对称加密算法,生成JDK的私钥。 操作步骤是:
     *
     * <p>
     * 1: 先打开CMD黑窗口输入命令  :
     *
     * </br>
     * keytool -genkeypair -alias oauth2 -keyalg RSA -keypass oauth2 -keystore oauth2.jks -storepass oauth2
     * </br>
     * 然后会产生一个黑窗口,一直下一步即可。最后输入Y 确定生成,然后会在你的C盘用户下面生成一个oauth2.jks的文件,
     * 将此文件拷贝到你的项目资源路径下,也就是resources下面,注意别打开也别修改
     * 这个是你的密钥,需要对应的公钥来解密。
     * <p>
     * 这行命令的意思是: 别名为 oauth2，秘钥算法为 RSA，秘钥口令为 oauth2，秘钥库（文件）名称为 oauth2.jks，秘钥库（文
     * 件）口令为 oauth2。输入命令回车后，后面还问题需要回答，最后输入 y 表示确定。
     * <p>
     * 这写值都可以自己去修改,并不是强制规定,但是要记住哦~~ 然后通过类路径加载oauth2.jks文件,
     * 具体参考 {@link org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory}
     * 使用密钥库,通过类路径将生成的oauth2.jks加载进来,第二个参数为密钥库的口令。
     * 然后通过jwtAccessTokenConverter的setKeyPair方法设置密钥对
     * </P>
     *
     * @return
     */
//    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter2() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(new ClassPathResource("oauth2.jks"), KEY_PASSWORD.toCharArray());
        jwtAccessTokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair(KEY_PASSWORD));
        return jwtAccessTokenConverter;
    }


    /**
     * 创建JWT令牌转换器
     *
     * SigningKey 的作用是？
     */
    // @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter3(){
        /**
         * 设置JWT令牌的签名key
         */
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey("signingKey");
        return converter;
    } // https://blog.csdn.net/weixin_44516305/article/details/88887229

}
