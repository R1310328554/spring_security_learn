package com.okta.spring.AuthorizationServerApplication.cfg.refresh;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 从请求头中获取客户端id和客户端密钥
 *
 * @author July
 */
public class RequestUtil {

    public static String[] extractAndDecodeHeader(String header) throws IOException {
        // `Basic ` 后面开始截取 clientId:clientSecret
        byte[] base64Token = header.trim().substring(6).getBytes(StandardCharsets.UTF_8);

        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException var8) {
            throw new RuntimeException("请求头解析失败：" + header);
        }

        String token = new String(decoded, StandardCharsets.UTF_8);
        int delim = token.indexOf(":");
        if (delim == -1) {
            throw new RuntimeException("请求头无效：" + token);
        } else {
            return new String[]{token.substring(0, delim), token.substring(delim + 1)};
        }
    }
}

