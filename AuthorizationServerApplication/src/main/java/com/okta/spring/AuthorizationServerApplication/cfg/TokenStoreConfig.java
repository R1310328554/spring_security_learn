//package com.okta.spring.AuthorizationServerApplication.cfg;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.AuthorityUtils;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//import org.springframework.security.oauth2.common.OAuth2AccessToken;
//import org.springframework.security.oauth2.provider.OAuth2Authentication;
//import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
//import org.springframework.security.oauth2.provider.token.TokenStore;
//import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
//import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
//import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import java.security.KeyPair;
//import java.util.Collection;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class TokenStoreConfig {
//
//    @Bean("keyProp")
//    public KeyProperties keyProperties() {
//        return new KeyProperties();
//    }
//    //前文中生成的 test.keystore 文件在resources资源文件夹下的路径
//    private String location = "test.keystore";
//    // 生成 test.keystore 文件时候所用的【密钥库口令】
//    private String secret = "storepass";
//     // 生成 test.keystore 文件时候所用的【别名】
//    private String alias = "alias";
//    // 生成 test.keystore 文件时候所用的【私钥的密码】
//    private String password = "keypass";
//    // 生成的秘钥中的 PUBLIC KEY 部分
//    private String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
//            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsn4FPljPEtJCDmXMEoEK\n" +
//            "xLCMl5rmd8MUxFVBhwUZGSH71h1Zphaxm9EvSulJQm+FzcldJYTeIDcQYtEl1kbQ\n" +
//            "4Fmq840Iy3JSLF1Z9WXpddIzB4PLkrJwFYAXZAR351jbYGqUbzWWjJEOGLP6Yvzb\n" +
//            "BrN6DzEYamoSwGUl8R93h1oRUHR3tVqdbMcjmyllvY7N9Qgk9lAldm6CcgavMqbm\n" +
//            "USp2X64CgAtdwDixWSO3eCucpXX6ocwaEnUB4XWTXl76OhilZ0WSXXHXOuDZGllj\n" +
//            "7fhmL0Hyo4bfy3PuQ9BdCJqE54SIELGxrTvapgLqwWhngJlnHSAGqRMkNxCFmwCk\n" +
//            "/wIDAQAB\n" +
//            "-----END PUBLIC KEY-----";
//
//    /**
//     * TokenStore是一个接口，他有五个直接子类，分别是：
//     * JwtTokenStore jwt方式存储
//     * JdbcTokenStore 数据库方式存储
//     * InMemoryTokenStore 内存方式存储
//     * 这里指定了是以JWT方式存储
//     */
//    @Bean
//    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
//        //JWT方式存储，需要JWT令牌转换器，下面jwtAccessTokenConverter()方法中自定义了一个JWT令牌转换器
//        return new JwtTokenStore(jwtAccessTokenConverter);
//    }
//
//    /**
//     * 自定义JWT令牌转换器，交给Spring
//     * JWT令牌转换器需要密钥对
//     * 密钥对需要密钥文件，即（ .jks 或者 .keystore） 文件，该文件里保存了密钥信息,密钥包含了私钥和公钥
//     *
//     * @return
//     */
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter() {
//
//        JwtAccessTokenConverter jwtAccessTokenConverter = new MyJwtAccessTokenConverter();
//        // ================= 加密相关配置 =====================
//        ClassPathResource resource = new ClassPathResource(location);
//        KeyStoreKeyFactory keyStoreFactory = new KeyStoreKeyFactory(resource, secret.toCharArray());
//        KeyPair keyPair = keyStoreFactory.getKeyPair(alias, password.toCharArray());
//        //为自定义的JWT令牌转换器设置密钥对（密钥对包含了公钥和私钥，公钥用于加密令牌，私钥用于解密令牌）
//        jwtAccessTokenConverter.setKeyPair(keyPair);
//
//        // ================= 解密相关配置 ======================
//        DefaultAccessTokenConverter defaultAccessTokenConverter = new DefaultAccessTokenConverter();
//        defaultAccessTokenConverter.setUserTokenConverter(new MyDefaultUserAuthenticationConverter());
//        jwtAccessTokenConverter.setAccessTokenConverter(defaultAccessTokenConverter);
//        jwtAccessTokenConverter.setVerifierKey(publicKey);
//
//        return jwtAccessTokenConverter;
//    }
//
//    class MyDefaultUserAuthenticationConverter extends DefaultUserAuthenticationConverter {
//    	//在token的解密过程中，可以在extractAuthentication()方法中处理从token中解析出来的用户信息
//        @Override
//        public Authentication extractAuthentication(Map<String, ?> map) {
//            if (map.containsKey(USERNAME)) {
//                List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils
//                        .collectionToCommaDelimitedString((Collection<?>) map.get(AccessTokenConverter.AUTHORITIES)));
//                // CustomUserDetails 是自定义的实体类，继承了 User 类，实现自定义用户信息
//                CustomUserDetails userDetails = new CustomUserDetails((String) map.get(USERNAME), "admin", authorities);
//                userDetails.setOrganizationName("智商有限公司");
//                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, "N/A", authorities);
//                return usernamePasswordAuthenticationToken;
//            }
//            return null;
//        }
//    }
//
//
//    class MyJwtAccessTokenConverter extends JwtAccessTokenConverter {
//		//在token的加密过程中，可以在enhance()方法中处理即将加密到token中的用户信息
//        @Override
//        public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
//            DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(accessToken);
//            Map<String, Object> info = new LinkedHashMap<String, Object>(accessToken.getAdditionalInformation());
//            String tokenId = result.getValue();
//            if (!info.containsKey(TOKEN_ID)) {
//                info.put(TOKEN_ID, tokenId);
//            }
//            // ====================================
//            info.put("age", "18");//添加年龄，18岁
//            // ====================================
//            result.setAdditionalInformation(info);
//            //super.encode(result, authentication) 用于加密信息
//            result.setValue(super.encode(result, authentication));
//            result.setRefreshToken(accessToken.getRefreshToken());
//            result.setExpiration(accessToken.getExpiration());
//            return result;
//        }
//    }
//}
