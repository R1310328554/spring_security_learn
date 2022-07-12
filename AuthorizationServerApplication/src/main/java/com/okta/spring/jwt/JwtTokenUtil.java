package com.okta.spring.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.binary.Base64;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenUtil {


    private Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private  SignatureAlgorithm hs256 = SignatureAlgorithm.HS256;

    //private  SecretKey ltcsecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode("ltc"));
    //cuAihCz53DZRjZwbsGcZJ2Ai6At+T142uphtJMsk7iQ=

    public static SecretKey generalKey(){
        byte[] encodedKey = Base64.decodeBase64("cuAihCz53DZRjZwbsGcZJ2Ai6At+T142uphtJMsk7iQ=");//自定义
        SecretKey key = Keys.hmacShaKeyFor(encodedKey);
        return key;
    }

    public static SecretKey generalKeyByDecoders(){

        return Keys.hmacShaKeyFor(Decoders.BASE64.decode("cuAihCz53DZRjZwbsGcZJ2Ai6At+T142uphtJMsk7iQ="));

    }


    public String createToken(String userid, String role){
        //创建token,在token中加入必备信息
//        String token = Jwts.builder()
//                .setId(userid)
//                .signWith(secretKey)
//                .setIssuedAt(new Date()) //设置创建时间戳
////                .claim("role",role)
//                .claim(userid,role)
//                .compact();

        Date now = new Date();
        // 过期时间
        Date expireDate = new Date(now.getTime() + 1000 * 60 * 24 * 1000);
        //创建Signature SecretKey
//        final SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        //header参数
        final Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("alg", "HS256");
        headerMap.put("typ", "JWT");


        //生成token
        String token = Jwts.builder()
                .setSubject(userid)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .setIssuer("luo")
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return token;
    }


    /**
     * 验证token是否还有效
     *
     * @param token       客户端传入的token
     * @param userDetails 从数据库中查询出来的用户信息
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = getUserNameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * 判断token是否已经失效
     */
    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }

    /**
     * 从token中获取过期时间
     */
    private Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }

    /**
     * 从token中获取登录用户名
     */
    public String getUserNameFromToken(String token) {
        String username;
        try {
            Claims claims = getClaimsFromToken(token);
            username =  claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }

    /**
     * 从token中获取JWT中的负载
     */
    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {

            claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
//            claims = Jwts.parser()
//                    .setSigningKey(secretKey)
//                    .parseClaimsJws(token)
//                    .getBody();
        } catch (Exception e) {
            // LOGGER.info("JWT格式验证失败:{}",token);
            e.printStackTrace();
        }
        return claims;
    }


    public Claims parseToken(String token){
        //顺带说一句，当Jwt设置了有效期，有效期时间过了之后也会抛出异常，解决办法是try
        //catch一下，将异常抛给统一异常处理类
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        }catch (JwtException e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    public static void main(String[] args) {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhIiwiaWF0IjoxNjU3NTA4NzE4LCJ1c2VyaWQiOiJhIn0.KHJS13RJqcGpP7796CnGCyoTrCsGizp7QNQGf2IBJJQ";
        JwtTokenUtil jwt = new JwtTokenUtil();
        token = jwt.createToken("aa", "admin");
        System.out.println("token1 = " + token);

        String username = jwt.getUserNameFromToken(token);
        System.out.println("username = " + username);
//        jwt.validateToken(token);


    }
}
