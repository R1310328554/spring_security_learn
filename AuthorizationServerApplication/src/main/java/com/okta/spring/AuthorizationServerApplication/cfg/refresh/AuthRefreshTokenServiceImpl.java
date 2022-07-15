package com.okta.spring.AuthorizationServerApplication.cfg.refresh;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.arronlong.httpclientutil.HttpClientUtil;
import com.arronlong.httpclientutil.common.HttpConfig;
import com.arronlong.httpclientutil.common.HttpHeader;
import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.okta.spring.jwt.domain.CommonResult;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
//import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.client.ServiceInstance;
//import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 七月初七
 * @version 1.0
 * @date 2021/12/10  18:16
 */
@Service
@Slf4j
@ApiModel(value = "自定义刷新令牌逻辑")
public class AuthRefreshTokenServiceImpl implements AuthRefreshTokenService {

    @Value("${user.oauth.selfLanUrl:}")
    private String selfLanUrl;

    /**
     * 授予类型
     */
    public static final String GRATE_TYPE = "grant_type";


    /**
     * 刷新令牌
     */
    public static final String REFRESH_TOKEN = "refresh_token";

    /**
     * 授权服务器微服务唯一id
     */
    public static final String SERVICE_ID = "auth-service";

    /**
     * 刷新令牌访问接口地址
     */
    public static final String REFREASH_URL = "/auth/oauth/token";

    /**
     * 如果请求中包含error,则说明请求失败
     */
    public static final String ERROR_NAME = "error";


    /**
     * 带有负载均衡的客户端信息
    @Autowired
    private LoadBalancerClient loadBalancerClient;
     */

    /**
     * 自定义刷新令牌逻辑
     *
     * @param header       请求头
     * @param refreshToken 刷新令牌
     * @return
     * @throws HttpProcessException
     */
    @Override
    public CommonResult<?> getRefreshToken(String header, String refreshToken) throws HttpProcessException {

        /**
         * 此方法获取的是一个微服务名称
        ServiceInstance serviceInstance = loadBalancerClient.choose(SERVICE_ID);
        if (serviceInstance == null) {
            return CommonResult.fail("为找到有效的认证服务器!");
        }
        log.info("主机=======>:{}", serviceInstance.getHost());
        log.info("实例id=======>:{}", serviceInstance.getInstanceId());
        log.info("端口=======>:{}", serviceInstance.getPort());
        log.info("URI=======>:{}", serviceInstance.getUri());
        log.info("微服务id=======>:{}", serviceInstance.getServiceId());
        log.info("方案=======>:{}", serviceInstance.getScheme());
        log.info("元数据=======>:{}", serviceInstance.getMetadata());
        String uri = serviceInstance.getUri().toString();
         */

        //拼接请求刷新令牌的地址: URI
        String uri = selfLanUrl;
        if (uri.endsWith("/")) {
            uri = uri.substring(0, uri.length() - 1);
        }
        String refreshTokenUrl = uri + REFREASH_URL;
        log.info("refreshTokenUrl==========>{}", refreshTokenUrl);


        /**
         *  封装请求刷新令牌的参数
         *  1: grant_type: refresh_token
         *  2: refresh_token: refreshToken
         */

        Map<String, Object> map = new HashMap<>(16);
        map.put(GRATE_TYPE, REFRESH_TOKEN);
        map.put(REFRESH_TOKEN, refreshToken);


        //  构建配置请求参数(网址、请求参数、编码、client)

        Header[] headers = HttpHeader
                .custom()
                //application/x-www-form-urlencoded
                .contentType(HttpHeader.Headers.APP_FORM_URLENCODED)
                //认证请求头 Authorization
                .authorization(header)
                .build();


        //  配置请求头所需的参数
        HttpConfig httpConfig = HttpConfig.custom()
                .headers(headers)
                .url(refreshTokenUrl)
                .map(map);


        //发送请求,响应令牌
        String token = HttpClientUtil.post(httpConfig);

        log.info("token=========>{}", token);

        //解析令牌 获取到了新的令牌的所有信息
        JSONObject parseObject = JSON.parseObject(token);
        log.info("parseObject=========>{}", parseObject);

        String error = parseObject.getString(ERROR_NAME);
        log.error("ERROR=====>{}", error);
        if (StringUtils.isNotBlank(error)) {
            return CommonResult.failed("无请求头信息!" + error);
        }

        // 将数据返回
        return CommonResult.success(parseObject);
    }
}

