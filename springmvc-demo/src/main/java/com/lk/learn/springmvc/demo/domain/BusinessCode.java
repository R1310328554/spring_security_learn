package com.lk.learn.springmvc.demo.domain;

public enum BusinessCode implements ReturnCode {


    SUCCESS(0, "success"),
    SYSTEM_ERROR(10000, "未知系统错误"),
    MISS_PARAM(10001, "缺少参数"),
    PARAM_TYPE_IS_NOT_MATCH(10002, "参数类型不匹配"),
    FILE_UPLOAD_ERROR(10003, "文件上传错误"),
    UNKNOWN_ERROR(20000, "未知业务失败"),
    PARAM_TYPE_IS_INVALID(10005, "参数校验不通过"),

    NOTFOUND_ERROR(404, "NOTFOUND_ERROR失败");


    /**
     * 业务编号
     */
    private int code;

    /**
     * 业务值
     */
    private String message;

    BusinessCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 获取返回编码
     *
     * @return code
     */
    @Override
    public int code() {
        return code;
    }

    /**
     * 获取返回描述信息
     *
     * @return message
     */
    @Override
    public String message() {
        return message;
    }
}
