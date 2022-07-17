package com.lk.learn.springmvc.demo;

import java.io.BufferedReader;

import java.io.IOException;

import java.io.InputStream;

import java.io.InputStreamReader;

import java.io.OutputStream;

import java.nio.charset.Charset;



import org.springframework.http.HttpInputMessage;

import org.springframework.http.HttpOutputMessage;

import org.springframework.http.MediaType;

import org.springframework.http.converter.AbstractHttpMessageConverter;

import org.springframework.http.converter.HttpMessageNotReadableException;

import org.springframework.http.converter.HttpMessageNotWritableException;


public class JaksonConverter extends AbstractHttpMessageConverter<Object> {

    public static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");

    public JaksonConverter() {
        super(new MediaType("application", "json", DEFAULT_CHARSET));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        // TODO Auto-generated method stub
        return false;
    }


    @Override
    protected Object readInternal(Class<? extends Object> clazz,
            HttpInputMessage inputMessage) throws IOException,
            HttpMessageNotReadableException {
        logger.info(clazz.getSimpleName());
        InputStream inputStream=inputMessage.getBody();
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        while((line = bufferedReader.readLine()) != null){
            stringBuilder.append(line);
        }
        logger.info(stringBuilder.toString());
        return stringBuilder;
    }


    @Override
    protected void writeInternal(Object t, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        logger.info(t.getClass().getSimpleName());

        // HashMap
        // JSONObject
        logger.info(t.toString());
        OutputStream os=outputMessage.getBody();
        os.write(t.toString().getBytes("utf-8"));
        os.flush();
    }
}
