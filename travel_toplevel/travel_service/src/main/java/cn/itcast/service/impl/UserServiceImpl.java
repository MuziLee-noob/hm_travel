package cn.itcast.service.impl;

import cn.itcast.dao.UserDao;
import cn.itcast.domain.User;
import cn.itcast.service.UserService;
import cn.itcast.util.MailUtils;
import cn.itcast.util.UUIDUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 注册用户
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {
        List<User> users = userDao.findUserByName(user.getUsername());
        if (!users.isEmpty()){
            //用户名已被使用
            return false;
        } else {
            //用户名可以使用
            //给用户设置未验证状态和验证码
            user.setStatus("N");
            user.setCode(UUIDUtils.getUUID());
            //保存用户
            userDao.saveUser(user);
            //发送邮件
            String content="<a href='http://localhost/travel/user/activate?code=" + user.getCode() + "'>点击激活【黑马旅游网】</a>";
            MailUtils.sendMail(user.getEmail(), content, "激活邮件");
            return true;
        }

    }

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public User login(String username, String password) {
        return userDao.login(username, password);
    }

    /**
     * 激活用户
     * @param code uuid
     * @return 激活是否成功
     */
    @Override
    public boolean active(String code) {
        //1.根据激活码查询用户对象
        User user = userDao.findByCode(code);
        if (user != null) {
            user.setStatus("Y");
            userDao.updateUserStatus(user);
            return true;
        }else {
            return false;
        }
    }
}
