package com.yancy0109.cloud.common;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Common返回结果
 * @author yancy0109
 */
@Data
@NoArgsConstructor
public class CommonResult {

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 返回信息
     */
    private String msg;

    /**
     * 携带数据
     */
    private Object data;

    /**
     * 手动构造方法
     */
    public CommonResult(Integer code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功结果
     */
    public static CommonResult success(){
        return new CommonResult(
                ResultEnum.SUCCESS.getCode(),
                ResultEnum.SUCCESS.getMsg(),
                null
        );
    }
    /**
     * 成功结果
     */
    public static CommonResult success(Object data){
        return new CommonResult(
                ResultEnum.SUCCESS.getCode(),
                ResultEnum.SUCCESS.getMsg(),
                data
             );
    }

    /**
     * 错误结果
     */
    public static CommonResult error(){
        return new CommonResult(
                ResultEnum.ERROR.getCode(),
                ResultEnum.ERROR.getMsg(),
                null
        );
    }

    /**
     * 自定义信息错误结果
     */
    public static CommonResult error(String msg){
        return new CommonResult(
                ResultEnum.ERROR.getCode(),
                msg,
                null
        );
    }
}
