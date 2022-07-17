package com.lk.learn;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.AccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.implicit.ImplicitResourceDetails;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * 自定义登录接口和更新接口
 * 此处相当于一个oauth的客户端，调用授权服务器 /oauth/token，使用密码模式获取访问令牌
 */
@RestController
@RequestMapping("/free/")
public class LoginController {

//    @Autowired
//    private OAuth2ClientProperties oAuth2ClientProperties;

    @Value("${user.oauth.clientId}")
    private String ClientID;
    @Value("${user.oauth.clientSecret}")
    private String ClientSecret;
    @Value("${user.oauth.serverUrl}")
    private String oauthServerUrl;
    @Value("${user.oauth.redirectUris}")
    private String redirectUris;

    // @Value("${security.oauth2.access-token-uri:}")
    @Value("${user.oauth.serverUrl}oauth/token")
    private String accessTokenUri;//
    // private String accessTokenUri = oauthServerUrl + "oauth/token";// 如果有赋值，那么 @Value 不会起作用..

    /**
     * http://192.168.1.103:8083/res/free/login?username=a&password=1
     *
     * @param username
     * @param password
     * @return
     */
    @RequestMapping("login")
    public OAuth2AccessToken login(@RequestParam("username") String username,
                                   @RequestParam("password") String password){
        // 创建 ResourceOwnerPasswordResourceDetails 对象，填写密码模式授权需要的请求参数
        ResourceOwnerPasswordResourceDetails resourceDetails=new ResourceOwnerPasswordResourceDetails();
       // String accessTokenUri = oauthServerUrl + "oauth/token";
        resourceDetails.setAccessTokenUri(accessTokenUri);
        resourceDetails.setClientId(ClientID);
        resourceDetails.setClientSecret(ClientSecret);
        resourceDetails.setUsername(username);
        resourceDetails.setPassword(password);
        // 创建 OAuth2RestTemplate 对象，是 Spring Security OAuth 封装的工具类，用于请求授权服务器
        OAuth2RestTemplate restTemplate=new OAuth2RestTemplate(resourceDetails);
        // 将 ResourceOwnerPasswordAccessTokenProvider 设置到其中，表示使用密码模式授权
        restTemplate.setAccessTokenProvider(new ResourceOwnerPasswordAccessTokenProvider());
        // 获取访问令牌
        return restTemplate.getAccessToken();
    }

    /*
        不测试通过 todo
        A redirect is required to get the users approval
     */
    @RequestMapping("login2")
    public OAuth2AccessToken login2(@RequestParam("username") String username,
                                   @RequestParam("password") String password){
        // 创建 ResourceOwnerPasswordResourceDetails 对象，填写密码模式授权需要的请求参数
        AuthorizationCodeResourceDetails resourceDetails = new AuthorizationCodeResourceDetails();
        //String accessTokenUri = oauthServerUrl + "oauth/token";
        resourceDetails.setAccessTokenUri(accessTokenUri);
        resourceDetails.setClientId(ClientID);
        resourceDetails.setClientSecret(ClientSecret);

        resourceDetails.setUseCurrentUri(true);
        resourceDetails.setPreEstablishedRedirectUri(redirectUris);
        //resourceDetails.setUseCurrentUri(" ");
//        resourceDetails.setUsername(username);
//        resourceDetails.setPassword(password);
        // 创建 OAuth2RestTemplate 对象，是 Spring Security OAuth 封装的工具类，用于请求授权服务器
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails);
        // 将 ResourceOwnerPasswordAccessTokenProvider 设置到其中，表示使用密码模式授权
        AuthorizationCodeAccessTokenProvider accessTokenProvider = new AuthorizationCodeAccessTokenProvider();
        // accessTokenProvider.set
        restTemplate.setAccessTokenProvider(accessTokenProvider);

        // A redirect is required to get the users approval 必须要重定向！
        // 获取访问令牌
        return restTemplate.getAccessToken();
    }

    /*
        不测试通过 todo
        java.lang.IllegalStateException: No redirect URI available in request
     */
    @RequestMapping("login3")
    public OAuth2AccessToken login3(@RequestParam("username") String username,
                                   @RequestParam("password") String password){
        // 创建 ResourceOwnerPasswordResourceDetails 对象，填写密码模式授权需要的请求参数
        ImplicitResourceDetails resourceDetails = new ImplicitResourceDetails();
       // String accessTokenUri = oauthServerUrl + "oauth/token";
        resourceDetails.setAccessTokenUri(accessTokenUri);
        resourceDetails.setClientId(ClientID);
        resourceDetails.setClientSecret(ClientSecret);
        resourceDetails.setUseCurrentUri(true);
        resourceDetails.setGrantType("implicit");

        resourceDetails.setPreEstablishedRedirectUri(redirectUris);
        // The URI to which the user is to be redirected to authorize an access token.
        resourceDetails.setUserAuthorizationUri(redirectUris);
        //resourceDetails.setUseCurrentUri(" ");
//        resourceDetails.setUsername(username);
//        resourceDetails.setPassword(password);
        // 创建 OAuth2RestTemplate 对象，是 Spring Security OAuth 封装的工具类，用于请求授权服务器
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails);
        // 将 ResourceOwnerPasswordAccessTokenProvider 设置到其中，表示使用密码模式授权
        restTemplate.setAccessTokenProvider(new ImplicitAccessTokenProvider());
        // 获取访问令牌
        return restTemplate.getAccessToken();
    }

    /*
        测试通过
     */
    @RequestMapping("login4")
    public OAuth2AccessToken login4(@RequestParam("username") String username,
                                   @RequestParam("password") String password){
        // 创建 ResourceOwnerPasswordResourceDetails 对象，填写密码模式授权需要的请求参数

        // ClientCredentials 方式访问的token 不包含 refresh token
        ClientCredentialsResourceDetails resourceDetails = new ClientCredentialsResourceDetails();
        //String accessTokenUri = oauthServerUrl + "oauth/token";
        resourceDetails.setAccessTokenUri(accessTokenUri);
        resourceDetails.setClientId(ClientID);
        resourceDetails.setClientSecret(ClientSecret);
        //resourceDetails.setUseCurrentUri(" ");
//        resourceDetails.setUsername(username);
//        resourceDetails.setPassword(password);
        // 创建 OAuth2RestTemplate 对象，是 Spring Security OAuth 封装的工具类，用于请求授权服务器
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resourceDetails);
        // 将 ResourceOwnerPasswordAccessTokenProvider 设置到其中，表示使用密码模式授权
        restTemplate.setAccessTokenProvider(new ClientCredentialsAccessTokenProvider());
        // 获取访问令牌
        return restTemplate.getAccessToken();
    }

    @PostMapping("refresh")
    public Object refresh(@RequestParam("refresh_token") String refreshToken){
        RestTemplate restTemplate=new RestTemplate();
        HttpHeaders headers=new HttpHeaders();
        headers.setBasicAuth(ClientID, ClientSecret);
        String url=accessTokenUri+"?grant_type=refresh_token&refresh_token="+refreshToken;
        HttpEntity<HashMap<String, Object>> request = new HttpEntity<>(headers);
        JSONObject jsonObject=restTemplate.postForObject(url,request,JSONObject.class);
        return jsonObject;
    }
}
