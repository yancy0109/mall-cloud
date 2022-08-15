package com.yancy0109.cloud.controller;

import com.yancy0109.cloud.common.CommonResult;
import com.yancy0109.cloud.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

/**
 * 负载均衡处理
 * @author yancy0109
 */
@RestController
@RequestMapping("/user")
public class UserRibbonController {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${service-url.user-service}")
    private String userServiceUrl;

    @GetMapping("{id}")
    public CommonResult getUser(@PathVariable Long id){
        return restTemplate
                .getForObject(userServiceUrl + "/user/{1}",
                        CommonResult.class, id);
    }

    @GetMapping("/getByUsername")
    public CommonResult getByUsername(@RequestParam String username) {
        return restTemplate
                .getForObject(userServiceUrl + "/user/getByUsername?username={1}",
                        CommonResult.class, username);
    }

    @GetMapping("/getEntityByUsername")
    public CommonResult getEntityByUsername(String username){
        ResponseEntity<CommonResult> entity = restTemplate
                .getForEntity(
                        userServiceUrl + "/user/getByUseername?username={1}",
                        CommonResult.class, username
                );
        System.out.println(entity);
        if (entity.getStatusCode().is2xxSuccessful()){
            return entity.getBody();
        }
        return CommonResult.error();
    }

    @PostMapping("/create")
    public CommonResult create(@RequestBody UserModel user) {
        return restTemplate
                .postForObject(userServiceUrl + "/user/create",
                        user, CommonResult.class);
    }

    @PostMapping("/update")
    public CommonResult update(@RequestBody UserModel user) {
        return restTemplate
                .postForObject(userServiceUrl + "/user/update",
                user, CommonResult.class);
    }

    @PostMapping("/delete/{id}")
    public CommonResult delete(@PathVariable Long id) {
        return restTemplate
                .postForObject(userServiceUrl + "/user/delete/{1}",
                        null, CommonResult.class, id);
    }
}
