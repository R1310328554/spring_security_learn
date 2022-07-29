package com.lk.learn.springmvc.demo.testJ2ee;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Set;

/**
 * xss攻击的预防过滤器
 * Created by jiangkunkun on 2016/12/26
 *
 *
 // 弹框而已， 就算是攻击吗？ 不要觉得一个小弹窗不会造成什么危害，能够执行alert()函数就意味着通过这种方式可以执行任务JS代码。即攻击者通过javaScript几乎可以做任何事情：窃取用户的cookie和其他敏感数据，重定向到钓鱼网站，发送其他请求，执行注入转账、发布广告信息、在社交网站关注某个用户等。

 */
public class XssFilter implements Filter {
    FilterConfig filterConfig = null;
    /**
     * 需要匹配替换的正则表达式
     */
    private String regEx;
    /**
     * 过滤器排除url集合
     */
    private Set<String> excluded;

    /**
     * 过滤器初始化方法
     * @param filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        if (filterConfig.getInitParameter("regEx") != null) {
            regEx = filterConfig.getInitParameter("regEx");
        }
        if (filterConfig.getInitParameter("excluded") != null) {
            String excludedStr = filterConfig.getInitParameter("excluded");
            if (excludedStr.contains(",")) {
                String[] arr = excludedStr.split(",");
                for (String s : arr) {
                    excluded.add(s.trim());
                }
            } else {
                excluded.add(excludedStr);
            }
        }
    }
    //
    @Override
    public void destroy() {
        this.filterConfig = null;
    }
    //
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest inRequest = (HttpServletRequest) request;
        String uri = inRequest.getRequestURI();
        if (simpleMatch(excluded, uri)) {
            chain.doFilter(request, response);
            return;
        }
        chain.doFilter(new XssHttpServletRequestWrapper(
                inRequest,this.regEx), response);
    }

    private static boolean simpleMatch(Set<String> patterns, String str) {
        if (patterns == null || patterns.isEmpty() || str == null) {
            return false;
        }
        for (String pattern : patterns) {
            if (simpleMatch(pattern, str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * URL通配符匹配
     * @param pattern
     * @param str
     * @return
     */
    private static boolean simpleMatch(String pattern, String str) {
        if (pattern == null || str == null) {
            return false;
        }
        int firstIndex = pattern.indexOf('*');
        if (firstIndex == -1) {
            return pattern.equals(str);
        }
        if (firstIndex == 0) {
            if (pattern.length() == 1) {
                return true;
            }
            int nextIndex = pattern.indexOf('*', firstIndex + 1);
            if (nextIndex == -1) {
                return str.endsWith(pattern.substring(1));
            }
            String part = pattern.substring(1, nextIndex);
            int partIndex = str.indexOf(part);
            while (partIndex != -1) {
                if (simpleMatch(pattern.substring(nextIndex), str.substring(partIndex + part.length()))) {
                    return true;
                }
                partIndex = str.indexOf(part, partIndex + 1);
            }
            return false;
        }
        return (str.length() >= firstIndex &&
                pattern.substring(0, firstIndex).equals(str.substring(0, firstIndex)) &&
                simpleMatch(pattern.substring(firstIndex), str.substring(firstIndex)));
    }
}


//package com.jd.noah.cms.web.util;
//
//        import javax.servlet.http.HttpServletRequest;
//        import javax.servlet.http.HttpServletRequestWrapper;
//        import java.util.regex.Matcher;
//        import java.util.regex.Pattern;
//

/**
 * 用户请求包装类
 *  Created by jiangkunkun on 2016/12/26
 */
  class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
    /**
     * 需要匹配替换的正则表达式
     */
    private String regEx;

    public XssHttpServletRequestWrapper(HttpServletRequest servletRequest,String regEx) {
        super(servletRequest);
        this.regEx = regEx;
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = cleanXSS(values[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (value == null) {
            return null;
        }
        return cleanXSS(value);
    }

    //
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (value == null)
            return null;
        return cleanXSS(value);
    }

    /**
     * 清除替换XSS代码
     * @param value
     * @return
     */
    private String cleanXSS(String value) {
        value = value.replaceAll("<", "<").replaceAll(">", ">");
        value = value.replaceAll("\\(", "(").replaceAll("\\)", ")");
        value = value.replaceAll("'", "'");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "").trim();
/*        String regEx="[`~!@#$%^&*()+=|{}':;',//[//].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\"]";
        if(this.regEx != null){
            regEx = this.regEx;
        }
        Pattern p   =   Pattern.compile(regEx);
        Matcher dataMatcher   =   p.matcher(value);
        value = dataMatcher.replaceAll("").trim() ;*/
        return value;
    }


}
