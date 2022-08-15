package com.yancy0109.cloud.service.impl;

import com.yancy0109.cloud.entity.User;
import com.yancy0109.cloud.model.UserModel;
import com.yancy0109.cloud.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * UserService实现类
 * @author yancy0109
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * 简化Dao层
     */
    private static List<User> USER_LIST = new ArrayList<>();
    static {
        //初始化userList
        User user1 = new User(1L,"1","1");
        User user2 = new User(2L,"2","2");
        User user3 = new User(3L,"2","3");
        USER_LIST.add(user1);
        USER_LIST.add(user2);
        USER_LIST.add(user3);
    }

    @Override
    public boolean createUser(UserModel userModel) {
        return false;
    }

    @Override
    public List<UserModel> getAllUser() {
        List<UserModel> userModels = new ArrayList<>();
        USER_LIST.stream().forEach(
                user -> {
                    UserModel userModel = new UserModel();
                    BeanUtils.copyProperties(user,userModel);
                    userModels.add(userModel);
                }
        );
        return userModels;
    }

    @Override
    public UserModel getUserById(Long id) {
        User source = USER_LIST.stream().filter(
                user -> user.getId().equals(id)
        ).findFirst().orElse(null);
        if (Objects.nonNull(source)){
            return (UserModel) getTarget(
                    source,new UserModel()
            );
        }
        return null;
    }

    @Override
    public List<UserModel> getUserByIds(List<Long> ids) {
        List<User> collect = USER_LIST.stream().filter(
                user -> ids.contains(user.getId())
        ).collect(Collectors.toList());
        List<UserModel> result = collect.stream().map(user -> (UserModel)getTarget(user, new UserModel())).collect(Collectors.toList());
        return result;
    }

    @Override
    public UserModel getByUserName(String username) {
        User result = USER_LIST.stream().filter(
                user -> user.getUsername().equals(username)
        ).findFirst().orElse(null);
        if (Objects.isNull(result)){
            return null;
        }
        return (UserModel) getTarget(result,new UserModel());
    }

    @Override
    public UserModel updateUser(UserModel user) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        User userTarget = USER_LIST.stream().filter(
                user -> user.getId().equals(id)
        ).findFirst().orElse(null);
        if (Objects.isNull(userTarget)){
            return false;
        }
        USER_LIST = USER_LIST.stream().filter(
                user -> !user.getId().equals(id)
        ).collect(Collectors.toList());
        return true;
    }

    private Object getTarget(Object source, Object target){
        BeanUtils.copyProperties(source,target);
        return target;
    }
}
