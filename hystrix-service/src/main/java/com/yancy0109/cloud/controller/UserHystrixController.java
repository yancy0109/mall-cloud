package com.yancy0109.cloud.controller;

import cn.hutool.core.thread.ThreadUtil;
import com.yancy0109.cloud.common.CommonResult;
import com.yancy0109.cloud.model.UserModel;
import com.yancy0109.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * user-service服务调用接口
 * @author yancy0109
 */
@RestController
public class UserHystrixController {

    @Autowired
    private UserService userService;

    @GetMapping("/testCommand/{id}")
    public CommonResult testCommand(@PathVariable Long id){
        return userService.getUserCommand(id);
    }

    @GetMapping("/testException/{id}")
    public CommonResult testException(@PathVariable Long id){
        return userService.getUserException(id);
    }

    @GetMapping("/testCache/{id}")
    public CommonResult testCache(@PathVariable Long id){
        userService.getUserCache(id);
        userService.getUserCache(id);
        userService.getUserCache(id);
        return CommonResult.success();
    }
    @GetMapping("/testRemove/{id}")
    public CommonResult testRemoveCache(@PathVariable Long id){
        userService.getUserCache(id);
        userService.removeUserCache(id);
        userService.getUserCache(id);
        return CommonResult.success();
    }

    @GetMapping("/testCollapser")
    public CommonResult testCollapser() throws ExecutionException, InterruptedException {
        Future<UserModel> future1 = userService.getUserFuture(1L);
        Future<UserModel> future2 = userService.getUserFuture(1L);
        future1.get();
        future2.get();
        ThreadUtil.safeSleep(200);
        Future<UserModel> future3 = userService.getUserFuture(3L);
        future3.get();
        return CommonResult.success();
    }
}

