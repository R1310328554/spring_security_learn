package com.okta.spring.AuthorizationServerApplication;

import org.apache.tomcat.util.http.fileupload.IOUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Test {

    public static void main(String[] args) throws Exception {
        String redirect = "https://github.com/login/oauth/access_token?client_id=ee0e0710193b7cac1e68&redirect_uri=http://192.168.1.103:8999/v1/github/user/login&client_secret=d544353486c9e083d9c7437187236e8f191c6632&code=%s";
//        redirect = "p%3A2F2Flocalhost%3A8080++login++oauth2++code++github&response_type=code&scope=read%3Auser&state=V673lZDmlUFJPCeFiCkY79ydk51KGaQl4dliqKyyOSw--3D111%s";
        String code = "25271428ba17802ecc25";
        String accessTokenUrl = String.format(redirect, code);
//        String accessTokenUrl = redirect +  code;
//        System.out.println("accessTokenUrl = " + accessTokenUrl);
//        System.setProperty("http.proxySet", "true");
//        System.setProperty("http.proxyHost", "127.0.0.1");
//        System.setProperty("http.proxyPort", "8888");
//        System.setProperty("https.proxyHost", "127.0.0.1");
//        System.setProperty("https.proxyPort", "46487");

        URL baidu = new URL("http://192.168.1.103:8082/aa");
        URLConnection connection = baidu.openConnection();
        InputStream inputStream = connection.getInputStream();
        byte[] ba = new byte[inputStream.available()];
        IOUtils.readFully(inputStream, ba);
        System.out.println("new String(ba) = " + new String(ba));
//        HttpURLConnection connection = new

    }
}
