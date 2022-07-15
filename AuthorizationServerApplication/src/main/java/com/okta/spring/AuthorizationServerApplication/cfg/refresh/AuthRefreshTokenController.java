package com.okta.spring.AuthorizationServerApplication.cfg.refresh;

import com.arronlong.httpclientutil.exception.HttpProcessException;
import com.google.common.base.Preconditions;
import com.okta.spring.jwt.domain.CommonResult;
import io.swagger.annotations.ApiModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author 七月初七
 * @version 1.0
 * @date 2021/12/10  19:32
 */
@RestController
@ApiModel(value = "自定义刷新令牌控制层")
@Slf4j
public class AuthRefreshTokenController {


    /**
     * 请求头类型,一定要注意后面还有一个空格!!!!!!!!!!
     * 因为他的拼接顺序是 Basic +空格 + (客户端id + 客户端密钥生成的Base64编码)
     */
    public static final String HEADER_TYPE = "Basic ";


    @Autowired
    private AuthRefreshTokenService authRefreshTokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientDetailsService clientDetailsService;


    /**
     * 获取刷新令牌
     *
     * @param httpServletRequest
     * @return
     */
    @GetMapping("/user/refreshToken")
    public CommonResult<?> getRefreshToken(HttpServletRequest httpServletRequest) throws IOException {

        /**
         * 从请求头中获取刷新令牌
         */
        String refreshToken = httpServletRequest.getParameter("refreshToken");
        log.info("refreshToken=======>{}", refreshToken);

        /**
         * 判断请求头不能为空
         * 功能描述：检查boolean是否为真。 用作方法中检查参数
         * 失败时抛出的异常类型: IllegalArgumentException
         */
        Preconditions.checkArgument(StringUtils.isNotBlank(refreshToken));


        /**
         * 获取请求头
         */
        String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("请求头为:=============>{}", header);


        if (StringUtils.isEmpty(header) || !header.startsWith(HEADER_TYPE)) {
            throw new UnapprovedClientAuthenticationException("请求头中无clientId,请检查!");
        }

        String[] tokens = RequestUtil.extractAndDecodeHeader(header);
        assert tokens.length == 2;


        //获取客户端id
        String clientId = tokens[0];
        log.info("clientId===>{}", clientId);

        //获取客户端密钥
        String clientSecret = tokens[1];
        log.info("clientSecret===>{}", clientSecret);


        /**
         * 通过clientId查询客户端信息
         */
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

        if (null == clientDetails) {
            throw new UnapprovedClientAuthenticationException("clientId对应的客户端信息不存在[" + clientId + "]!!");
        }

        /**
         * 判断密码是否正确
         */
        if (!passwordEncoder.matches(clientSecret, clientDetails.getClientSecret())) {
            throw new UnapprovedClientAuthenticationException("客户端密码看起来不像是{ BCrypt }方式加密的!");
        }

        // 获取刷新令牌

        try {
            return authRefreshTokenService.getRefreshToken(header, refreshToken);
        } catch (HttpProcessException e) {
            e.printStackTrace();
            log.error("获取刷新令牌报错:{},具体错误为:{}", e.getMessage(), e);
            return CommonResult.failed("新令牌获取失败" + e.getMessage());
        }
    }
}

