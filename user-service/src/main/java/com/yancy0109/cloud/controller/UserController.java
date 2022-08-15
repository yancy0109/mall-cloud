package com.yancy0109.cloud.controller;

import com.yancy0109.cloud.common.CommonResult;
import com.yancy0109.cloud.model.UserModel;
import com.yancy0109.cloud.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 提供调用接口
 * @author yancy0109
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;
    @Autowired
    DiscoveryClient discoveryClient;

    @PostMapping("/create")
    public CommonResult create(@RequestBody UserModel user) {
        userService.createUser(user);
        return CommonResult.success();
    }

    @GetMapping("/{id}")
    public CommonResult getUser(@PathVariable Long id) {
        UserModel user = userService.getUserById(id);
        LOGGER.info("根据id获取用户信息，用户名称为：{}",user.getUsername());
        return CommonResult.success(user);
    }

    @GetMapping("/getUserByIds")
    public CommonResult getUserByIds(@RequestParam List<Long> ids) {
        List<UserModel> userList= userService.getUserByIds(ids);
        LOGGER.info("根据ids获取用户信息，用户列表为：{}",userList);
        return CommonResult.success(userList);
    }

    @GetMapping("/getByUsername")
    public CommonResult getByUsername(@RequestParam String username) {
        UserModel byUserName = userService.getByUserName(username);
        return CommonResult.success(byUserName);
    }

    @PostMapping("/update")
    public CommonResult update(@RequestBody UserModel user) {
        userService.updateUser(user);
        return CommonResult.success();
    }

    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        userService.delete(id);
        return CommonResult.success();
    }
}
