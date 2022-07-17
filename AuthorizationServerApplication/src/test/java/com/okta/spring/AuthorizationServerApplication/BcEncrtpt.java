package com.okta.spring.AuthorizationServerApplication;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcEncrtpt {
    public static void main(String[] args) {

        /*
            Spring Security 提供了多种密码加密方案，官方推荐使用 BCryptPasswordEncoder：
            BCryptPasswordEncoder 使用 BCrypt 强哈希函数，开发者在使用时可以选择提供 strength 和 SecureRandom 实例。
            strength 取值在 4~31 之间（默认为 10 xxx , 默认是 -1）。strength 越大，密钥的迭代次数越多（密钥迭代次数为 2^strength）
         */
        // 创建密码解析器
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        // 对密码进行加密
        String song = bCryptPasswordEncoder.encode("1");
        // 打印加密之后的数据
        System.out.println("加密之后数据：\t" + song);

        song = bCryptPasswordEncoder.encode("123");
        // 打印加密之后的数据
        System.out.println("加密之后数据：\t" + song);

        //判断原字符加密后和加密之前是否匹配
        boolean result = bCryptPasswordEncoder.matches("song", song);
        // 打印比较结果
        System.out.println("比较结果：\t" + result);

        String finalPassword = "{bcrypt}"+bCryptPasswordEncoder.encode("123456");
        System.out.println("finalPassword = " + finalPassword);

    }


    /*
    // 表示把参数按照特定的解析规则进行解析
String encode(CharSequence rawPassword);
// 表示验证从存储中获取的编码密码与编码后提交的原始密码是否匹配。如果密码匹配，则返回 true；如果不匹配，则返回 false。第一个参数表示需要被解析的密码。第二个参数表示存储的密码。
boolean matches(CharSequence rawPassword, String encodedPassword);
// 表示如果解析的密码能够再次进行解析且达到更安全的结果则返回 true，否则返回false。默认返回 false。
default boolean upgradeEncoding(String encodedPassword) {
return false;}


BCryptPasswordEncoder 是 Spring Security 官方推荐的密码解析器，平时多使用这个解析器。

BCryptPasswordEncoder 是对 bcrypt 强散列方法的具体实现。是基于 Hash 算法实现的单向加密。可以通过 strength 控制加密强度，默认 10.
————————————————
版权声明：本文为CSDN博主「prefect_start」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
原文链接：https://blog.csdn.net/prefect_start/article/details/125356882

     */
}
