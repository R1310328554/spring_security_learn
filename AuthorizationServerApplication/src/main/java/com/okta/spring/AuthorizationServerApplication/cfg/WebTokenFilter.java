
/*

最近项目中引入oauth2框架，发现token存在固定30分钟失效问题；而用户在实际使用过程中，如果固定30分钟就要登出，重新登录方能使用，体验度极差；需要后端能够提供token续签的功能；

        网上也搜索过不少资料，例如：

        后端提供刷新token接口，前端加入定时器，依赖后端返回的过期时间定时刷新token；

        但此方式无法满足当前项目的需要，项目允许同一个账号开启多个网页访问，需要登录；且前端使用的是本地session缓存，token只针对单个页面有效；同一个账号若是通过刷新token接口获取新的token，会导致其他界面的token失效；

        为了解决项目token续签问题，通过源码分析，获得了突破；

        思路一：如何实现token续签？
        用户登录成功后，会在redis中缓存key值：
            private static final String ACCESS = "access:";
            private static final String AUTH_TO_ACCESS = "auth_to_access:";
            private static final String AUTH = "auth:";
            private static final String CLIENT_ID_TO_ACCESS = "client_id_to_access:";
            private static final String UNAME_TO_ACCESS = "uname_to_access:";
        1
        2
        3
        4
        5
        这些key都是有过期时间的，若是想在原token的基础上实现自动续签，更新这几个key的过期时间就可以了；（后续实践证明除了要更新key的过期时间，还要更新对应的value里面的expiration）

        思路二：什么时间实现token续签？

        前端每一个请求都会携带token，在gateway里面增加一个过滤器，拦截所有的请求，对token进行解析，如果token还有10分钟（举例）过期，就重新设置token过期时间为30分钟；
        （此处不可每次请求都对token续签，效率很低）
        ————————————————
        版权声明：本文为CSDN博主「young」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
        原文链接：https://blog.csdn.net/weixin_44330810/article/details/122192302


 */

//package com.okta.spring.AuthorizationServerApplication.cfg;
//
//import com.alibaba.fastjson.JSON;
//
//import com.alibaba.fastjson.serializer.SerializerFeature;
//import com.google.gson.Gson;
//import com.netflix.zuul.ZuulFilter;
//import com.netflix.zuul.context.RequestContext;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.http.HttpStatus;
//import org.springframework.stereotype.Component;
//import org.springframework.ui.Model;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//@Slf4j
//@Component
//public class WebTokenFilter extends ZuulFilter {
//
//    private Gson gson = new Gson();
//
//    @Autowired
//    private RedisTemplate<String, String> redisTemplate;
//
//    @Resource
//    private CosSecurityProperties cosSecurityProperties;
//
//    @Override
//    public String filterType() {
//        return FilterConstants.PRE_TYPE;
//    }
//
//    @Override
//    public int filterOrder() {
//        return -1;
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        RequestContext ctx = RequestContext.getCurrentContext();
//        String url = ctx.getRequest().getRequestURI();
//        if(getPass(url)){//白名单url不校验token
//            return false;
//        }
//        return true;
//    }
//
//    public boolean getPass(String methodUrl){
//        for (String passUrl : cosSecurityProperties.getIgnoreUrlList()) {
//            if (methodUrl.contains(passUrl)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public Object run() {
//        log.info("---WebTokenFilter---");
//        RequestContext ctx = RequestContext.getCurrentContext();
//        Model error = checkToken(ctx);
//        if (error == null) {
//            ctx.set("isSuccess", true);
//        } else {
//            ctx.setSendZuulResponse(false);
//            ctx.set("isSuccess", false);
//            fillResponse(ctx, error);
//        }
//        return null;
//    }
//
//    /**
//     * 验证token
//     * @param ctx
//     * @return
//     */
//    private Model checkToken(RequestContext ctx) {
//        HttpServletRequest request = ctx.getRequest();
//        //验证请求token
//        String token = request.getHeader("Authorization");
//        try {
//            if (StringUtils.isBlank(token)) {
//                log.info("没有读取到token");
//                return null;
//            }
//            String username = tokenHandleUtil.getUsernameByToken(token);
//
//            if (StringUtils.isEmpty(username)) {
//                return null;
//            }
//            ctx.addZuulRequestHeader("username", username);
//        }catch (BusinessException e) {
//            log.error("【token】校验不通过，token=[{}],errorMessage=[{}]", token, e.getMessage());
//            log.error("error:",e);
//            ctx.setSendZuulResponse(false);
//            ctx.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
//            ResultMessage result = new ResultMessage(false, e.getErrMsg());
//            ctx.getResponse().setCharacterEncoding("UTF-8");
//            ctx.getResponse().setContentType("application/json; charset=utf-8");
//            ctx.setResponseBody(JSON.toJSONString(result, SerializerFeature.BrowserCompatible));
////            ctx.set(CosSecurityConstants.KEY_IS_SECURITY_PASS, false);
//        }
//        return null;
//    }
//
//    /**
//     * 设置response
//     *
//     * @param ctx
//     * @param error
//     */
//    private void fillResponse(RequestContext ctx, Model error) {
//        HttpServletRequest request = ctx.getRequest();
//        HttpServletResponse response = ctx.getResponse();
//        //序列化message
//        String message = gson.toJson(error);
//        log.info("response message:{}", message);
//
//        String contentType = request.getHeader("Content-Type");
//        String accept = request.getHeader("accept");
//        if ((contentType != null && contentType.toLowerCase().contains("application/json"))
//                || (accept  != null && accept.toLowerCase().contains("application/json"))) {
//            response.setContentType("application/json;charset=UTF-8");
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "*");
//            response.setHeader("Access-Control-Allow-Headers", "*");
//            ctx.setResponseBody(message);
//        } else {
//            ctx.setSendZuulResponse(false);
//            response.setContentType("text/html;charset=UTF-8");
//            response.setHeader("Access-Control-Allow-Origin", "*");
//            response.setHeader("Access-Control-Allow-Methods", "*");
//            response.setHeader("Access-Control-Allow-Headers", "*");
//            ctx.setResponseBody("<h3>error<h3></br>no Permission denied");
//        }
//    }
//}
//
