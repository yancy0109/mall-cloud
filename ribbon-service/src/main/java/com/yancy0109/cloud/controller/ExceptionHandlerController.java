package com.yancy0109.cloud.controller;

import com.yancy0109.cloud.common.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

//@RestControllerAdvice
public class ExceptionHandlerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(IllegalStateException.class)
    public CommonResult hander(IllegalStateException e){
        LOGGER.warn("节点连接错误 {} 错误类型 {}",new Date(),e.getClass());
        return CommonResult.error();
    }

}
