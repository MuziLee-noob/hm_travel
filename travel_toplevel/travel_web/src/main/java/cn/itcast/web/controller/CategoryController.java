package cn.itcast.web.controller;

import cn.itcast.domain.Category;
import cn.itcast.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService cs;

    @RequestMapping("/findAll")
    public List<Category> findAll(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (!(referer.endsWith("login.html") || referer.endsWith("register.html"))) {
            //1.调用service方法
            List<Category> list = cs.findAll();

            return list;
        }
        return null;
    }
}
