package com.yancy0109.cloud.common;

/**
 * 返回结果枚举类
 * @author yancy0109
 */
public enum ResultEnum {
    /**
     * 返回实例
     */
    SUCCESS(200,"操作成功"),
    ERROR(400,"操作失败");

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 返回信息
     */
    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
