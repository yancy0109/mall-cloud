package com.yancy0109.cloud.controller;

import com.yancy0109.cloud.common.CommonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author yancy0109
 */
@RestControllerAdvice
public class ExceptionHandlerController {


    @ExceptionHandler(RuntimeException.class)
    public CommonResult handler(RuntimeException e){
        return CommonResult.error(e.getMessage());
    }
}
