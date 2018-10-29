package cn.itcast.web.controller;

import cn.itcast.domain.User;
import cn.itcast.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 注册方法
     * @param user
     * @param check
     * @param session
     * @return
     */
    @RequestMapping("/register")
    public Map register(User user, String check, HttpSession session) {
        Map<String, Object> map = new HashMap<>();
        //验证验证码
        String checkCodeServer = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if (StringUtils.isEmpty(check) || checkCodeServer.isEmpty() || !checkCodeServer.equalsIgnoreCase(check)) {
            map.put("flag", false);
            map.put("errorMsg", "验证码输入错误");
            return map;
        }
        //调取service方法
        boolean result = userService.register(user);
        if (!result) {
            map.put("flag", false);
            map.put("errorMsg", "用户名已被使用");
        } else {
            map.put("flag", true);
        }
        return map;
    }

    /**
     * 登录方法
     * @param username
     * @param password
     * @param check
     * @param session
     * @return
     */
    @RequestMapping("/login")
    public Map login(String username, String password, String check, HttpSession session) {
        Map<String, Object> map = new HashMap<>();

        //验证验证码
        String checkCodeServer = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");
        if (StringUtils.isEmpty(check) || checkCodeServer.isEmpty() || !checkCodeServer.equalsIgnoreCase(check)) {
            map.put("flag", false);
            map.put("errorMsg", "验证码输入错误");
            return map;
        }

        User loginUser = userService.login(username, password);
        if (loginUser == null) {
            map.put("flag", false);
            map.put("errorMsg", "用户名或密码错误");
        } else {
            if ("N".equals(loginUser.getStatus())) {
                map.put("flag", false);
                map.put("errorMsg", "用户未激活，请登录邮箱激活后重试");
            } else {
                session.setAttribute("user", loginUser);
                map.put("flag", true);
            }
        }
        return map;
    }

    /**
     * 查找用户，用于header.html展示使用
     * @param session
     * @return
     */
    @RequestMapping("/findOne")
    public User findOne(HttpSession session) {
        return (User) session.getAttribute("user");
    }

    /**
     * 退出方法
     * @param session
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/exit")
    public void exit(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //销毁session
        session.invalidate();
        response.sendRedirect(request.getContextPath() + "/login.html");
    }

    /**
     * 激活方法
     * @param code 邮件中的uuid
     * @param response 呃
     * @throws IOException response.getWriter()抛出该异常
     */
    @RequestMapping("/activate")
    public void activate(String code,HttpServletRequest request, HttpServletResponse response) throws IOException {
        //判断code是否存在
        if (code != null) {
            //            UserService us = new UserServiceImpl();
            boolean flag = userService.active(code);

            //3.判断标记，响应不同信息
            String msg = null;
            response.setContentType("text/html;charset=utf-8");
            if (flag) {
                msg = "激活成功，请<a href='" + request.getContextPath() + "/login.html'>登录</a>";
                System.out.println(msg);
            } else {
                msg = "激活失败，请联系管理员";
            }
            response.getWriter().write(msg);
        }
    }
}
