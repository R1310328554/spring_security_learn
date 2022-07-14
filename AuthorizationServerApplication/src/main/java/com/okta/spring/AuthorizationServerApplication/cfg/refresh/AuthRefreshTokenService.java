package com.okta.spring.AuthorizationServerApplication.cfg.refresh;


import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.okta.spring.jwt.domain.CommonResult;

/**
 * @author 七月初七
 * @version 1.0
 * @date 2021/12/10  18:09
 */
public interface AuthRefreshTokenService {

    /**
     * 自定义获取刷新令牌
     *
     * <p>
     *     如果不自定义获取刷新令牌,那么访问报错的时候错误如下
     *
     *     {
     * 	    "timestamp": "2021-12-10T10:05:12.881+00:00",
     * 	    "status": 401,
     * 	    "error": "Unauthorized",
     * 	    "message": "Unauthorized",
     * 	    "path": "/auth/oauth/token"
     *     }
     *
     *     可是这种方式并不是我们想要的,我们想要的格式是
     *     {
     *         "code": "200",
     *         "message": "操作成功/失败",
     *         data:{
     *             "refresh_token": "xxxxxx",
     *             "access_token": "xxxxxx",
     *             "jti":   "xxxxx",
     *              .......
     *         }
     *     }
     * </p>
     *
     * @param header 请求头
     * @param refreshToken 刷新令牌
     * @return
     * @throws HttpProcessException
     */
    CommonResult<?> getRefreshToken(String header, String refreshToken) throws HttpProcessException;
}

