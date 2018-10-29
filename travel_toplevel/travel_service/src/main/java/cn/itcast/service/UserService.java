package cn.itcast.service;

import cn.itcast.domain.User;

public interface UserService {

    boolean register(User user);

    User login(String username, String password);

    boolean active(String code);
}
