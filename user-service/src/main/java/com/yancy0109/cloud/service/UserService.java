package com.yancy0109.cloud.service;


import com.yancy0109.cloud.model.UserModel;

import java.util.List;

/**
 * UserService
 * @author yancy0109
 */
public interface UserService {


    boolean createUser(UserModel userModel);

    List<UserModel> getAllUser();


    UserModel getUserById(Long id);

    List<UserModel> getUserByIds(List<Long> ids);

    UserModel getByUserName(String username);

    UserModel updateUser(UserModel user);

    boolean delete(Long id);
}
