//package com.lk.learn.springboot.demo.controller;
//
//import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
//import sun.misc.MessageUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import java.util.HashMap;
//import java.util.Map;
//
//public class LoginService {
//
//    /**
//     * 登录验证
//     *
//     * @param username 用户名
//     * @param password 密码
//     * @param code 验证码（整合keyclock后，code不再是验证码，而是keyclock的token）
//     * @param uuid 验证码唯一标识
//     * @return 结果
//     */
//    public String login(String username, String password, String code, String uuid)
//    {
//        //验证码验证
//        verifyLoginCode(username,code,uuid);
//        // 用户验证
//        Authentication authentication = null;
//        try
//        {
//            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
//            authentication = authenticationManager
//                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
//        }
//        catch (Exception e)
//        {
//            if (e instanceof BadCredentialsException)
//            {
//                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
//                throw new UserPasswordNotMatchException();
//            }
//            else
//            {
//                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
//                throw new CustomException(e.getMessage());
//            }
//        }
//        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        // 生成token
//        return tokenService.createKeycToken(loginUser,code);
//    }
//
//    /**
//     * 根据keyclock的token，生成与用户关联的token
//     * 用keyclock代替了 String token = IdUtils.fastUUID();
//     * @param loginUser 用户信息
//     * @param token keyclock生成的token
//     * @return
//     */
//    public String createKeycToken(LoginUser loginUser,String token)
//    {
//        // TODO
//        if(StringUtils.isNotEmpty(token)){
////            String token = code.substring(code.length()-48,code.length());
//            loginUser.setToken(token);
//            setUserAgent(loginUser);
//            refreshToken(loginUser);
//
//            Map<String, Object> claims2 = new HashMap<>();
//            claims2.put(Constants.LOGIN_USER_KEY, token);
//            createToken(claims2);
//            return createToken(claims2);
//        }
//        return null;
//
//    }
//
//    public LoginUser getLoginUser(HttpServletRequest request)
//    {
//        // TODO 拿到keycloak生成的token
//        String token = getToken(request);
//        if(StringUtils.isNotEmpty(token)){
//            // 这里截取一下，代替 String token = IdUtils.fastUUID();
//            // 为什么在这里需要创建一下token呢？因为我们用createKeycToken代替了createToken
////            String tok2 = token.substring(token.length()-48,token.length());
//            String tok2 = token;
//            Map<String, Object> claims2 = new HashMap<>();
//            claims2.put(Constants.LOGIN_USER_KEY, tok2);
//            String tok3 = createToken(claims2);
//            // 通过上边的createToken，找到相应的user信息
//            Claims claims3 = parseToken(tok3);
//            // 解析对应的权限以及用户信息
//            String uuid2 = (String) claims3.get(Constants.LOGIN_USER_KEY);
//            String userKey2 = getTokenKey(uuid2);
//            LoginUser user2 = redisCache.getCacheObject(userKey2);
//            return user2;
//        }
//
//        // 获取请求携带的令牌
////        String token = getToken(request);
////        if (StringUtils.isNotEmpty(token))
////        {
////            Claims claims = parseToken(token);
////            // 解析对应的权限以及用户信息
////            String uuid = (String) claims.get(Constants.LOGIN_USER_KEY);
////            String userKey = getTokenKey(uuid);
////            LoginUser user = redisCache.getCacheObject(userKey);
////            return user;
////        }
//        return null;
//    }
//
//
//}
