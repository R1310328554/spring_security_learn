package com.okta.spring.AuthorizationServerApplication.cfg;

import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>
 * 自定义扩展器,当认证成功后，如果客户端需要其他用户信息，则可以进行扩展
 * </p>
 *
 * @author 七月初七
 * @version 1.0
 * @date 2021/12/10  17:18
 */
@Configuration
public class JwtTokenEnhancer implements TokenEnhancer {

    /**
     * 对当前客户端提供自定义的用户数据返回,用于去做OAuth2身份认证
     *
     * OAuth2AccessToken: 它的实现类只有一个叫做 {@link DefaultOAuth2AccessToken} 中的 setAdditionalInformation()方法
     * 他的方法主要是以下作用:
     *  令牌授予者想要添加到令牌的附加信息，例如支持新的令牌类型。如果映射中的值是原始值
     *  则远程通信将始终有效。使用地图（如果需要，嵌套）或由 Jackson 明确序列化的东西也应该是安全的。
     *
     * @param accessToken    oauth2的token
     * @param authentication authentication.getPrincipal()获取当前登录的用户信息
     * @return
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        /**
         * 此处直接强转即可。因为我们知道当前的这个登录用户就是我们自定义的用户对象
         */
        //JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        Object jwtUser =  authentication.getPrincipal();
        Map<String, Object> map = new LinkedHashMap<>();
//        map.put("userInfo", JSON.toJSON(jwtUser));
        /**
         * 这样我们的accessToken就可以进行扩展信息了。然后将其配置到授权服务器中。{@link com.qycq.oauth.security.AuthorizationServerConfig}
         */
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(map);
        return accessToken;
    }
}

