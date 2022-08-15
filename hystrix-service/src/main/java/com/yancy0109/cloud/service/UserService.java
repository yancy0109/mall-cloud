package com.yancy0109.cloud.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import com.yancy0109.cloud.common.CommonResult;
import com.yancy0109.cloud.model.UserModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * UserService
 * @author yancy0109
 */
@Service
public class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    @Autowired
    RestTemplate restTemplate;
    @Value("${service-url.user-service}")
    private String userServiceUrl;

    @HystrixCommand(fallbackMethod = "getDefaultUser1",      //降级处理方法
                    commandKey = "getUserCommand",          //命令名称
                    groupKey = "getUserGroup",              //分钟名称
                    threadPoolKey = "getUserThreadPool"     //线程池名称
                    )
    public CommonResult getUserCommand(Long id) {
        return restTemplate.getForObject(
                userServiceUrl + "/user/{1}",
                CommonResult.class,
                id
        );
    }

    public CommonResult getDefaultUser1(@PathVariable Long id){
        UserModel defaultUser = new UserModel(-1L,"DefaultUser","123456");
        return CommonResult.success(defaultUser);
    }

    @HystrixCommand(fallbackMethod = "getDefaultUser2",
                    ignoreExceptions = {NullPointerException.class})
    public CommonResult getUserException(Long id) {
        if (id .equals(-1L)){
            throw new NullPointerException();
        }
        if (id.equals(-2L)){
            throw new IndexOutOfBoundsException();
        }
        LOGGER.info("发送正常请求,请求id为 {}",id);
        return restTemplate.getForObject(
                userServiceUrl + "/user/{1}",
                CommonResult.class,
                id
        );
    }

    public CommonResult getDefaultUser2(Long id){
        UserModel defaultUser = new UserModel(-2L,"DefaultUser","123456");
        return CommonResult.success(defaultUser);
    }

    @CacheResult(cacheKeyMethod = "getCacheKey")
    @HystrixCommand(fallbackMethod = "getDefaultUser1",
                    commandKey = "getUserCache")
    public CommonResult getUserCache(Long id) {
        LOGGER.info("getUserCache id:{}",id);
        return restTemplate.getForObject(
                userServiceUrl + "/user/{1}",
                CommonResult.class,
                id
        );
    }

    /**
     * 为缓存生成key的方法
     */
    public String getCacheKey(Long id){
//        LOGGER.info("getCacheKey: {}",id);
        return String.valueOf(id);
    }

    @CacheRemove(commandKey = "getUserCache",cacheKeyMethod = "getCacheKey")
    @HystrixCommand
    public CommonResult removeUserCache(Long id) {
        LOGGER.info("removeUserCache id: {}",id);
        return restTemplate.postForObject(
                userServiceUrl + "/user/delete/{1}",
                null,
                CommonResult.class,
                id
        );
    }
    @HystrixCollapser(batchMethod = "getUserByIds",collapserProperties = {
            @HystrixProperty(name = "timerDelayInMilliseconds", value = "100")
    })
    public Future<UserModel> getUserFuture(Long id) {
        return new AsyncResult<UserModel>(){
            @Override
            public UserModel invoke() {
                CommonResult commonResult = restTemplate.getForObject(userServiceUrl + "/user/{1}",
                        CommonResult.class,
                        id);
                Map data = (Map) commonResult.getData();
                UserModel user = BeanUtil.mapToBean(data,UserModel.class,true);
                LOGGER.info("getUserById username:{}", user.getUsername());
                return user;
            }
        };
//        return null;
    }

    @HystrixCommand
    public List<UserModel> getUserByIds(List<Long> ids){
        LOGGER.info("getUserByIds:{}",ids);
        CommonResult commonResult = restTemplate.getForObject(
                userServiceUrl + "/user/getUserByIds?ids={1}",
                CommonResult.class,
                CollUtil.join(ids, ",")
        );
        return (List<UserModel>)commonResult.getData();
    }

    public List<UserModel> getDefaultUserList(List<Long> ids){
        UserModel defaultUser = new UserModel(-2L,"DefaultUser","123456");
        ArrayList<UserModel> userModels = new ArrayList<>();
        userModels.add(defaultUser);
        return userModels;
    }
}

