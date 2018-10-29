package cn.itcast.web.controller;

import cn.itcast.domain.PageBean;
import cn.itcast.domain.Route;
import cn.itcast.domain.User;
import cn.itcast.service.FavoriteService;
import cn.itcast.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/route")
public class RouteController {

    @Autowired
    private RouteService routeService;

    @Autowired
    private FavoriteService favoriteService;

    /**
     * 根据条件查询线路列表
     *
     * @param rname
     * @param cid
     * @param currentPage
     * @param pageSize
     * @return
     * @throws IOException
     */
    @RequestMapping("/pageQuery")
    public PageBean<Route> pageQuery(String rname, String cid, String currentPage, String pageSize) throws IOException {

        if (rname != null && rname.length() != 0 && !"null".equals(rname)) {
            rname = new String(rname.getBytes("iso_8859_1"), StandardCharsets.UTF_8);
            rname = "%" + rname + "%";
        }
        //处理参数，将字符串转换为int类型

        int cid_int = 0;//类别id

        if (cid != null && cid.length() != 0 && !"null".equals(cid)) {
            cid_int = Integer.parseInt(cid);
        }
        int currentPage_int = 1;//如果不赋值，默认为1
        if (currentPage != null && currentPage.length() != 0 && !"null".equals(currentPage)) {
            currentPage_int = Integer.parseInt(currentPage);
        }
        int pageSize_int = 5;//如果不赋值，默认为5
        if (pageSize != null && pageSize.length() != 0) {
            pageSize_int = Integer.parseInt(pageSize);
        }
        PageBean<Route> pb = routeService.pageQuery(cid_int, currentPage_int, pageSize_int, rname);
        return pb;
    }

    /**
     * 根据rid查找一条线路的详细信息
     *
     * @param rid 线路id
     * @return
     */
    @RequestMapping("/findOne")
    public Route findOne(String rid) {

        return routeService.findOne(rid);
    }

    /**
     * 判断用户是否收藏当前路线
     *
     * @param rid
     * @param session
     * @return
     */
    @RequestMapping("/isFavorite")
    public boolean isFavorite(String rid, HttpSession session) {
        User user = (User) session.getAttribute("user");
        //获取uid
        int uid;
        if (user == null) {
            uid = 0;
        } else {
            uid = user.getUid();
        }

        //调用favoriteService查询是否收藏

        boolean flag = favoriteService.isFavorite(rid, uid);
        return flag;
    }

    /**
     * 设置收藏
     *
     * @param rid
     * @param session
     */
    @RequestMapping("/setFavorite")
    public void setFavorite(String rid, HttpSession session) {
        //获取当前登录用户
        User user = (User) session.getAttribute("user");

        //获取uid
        int uid;
        if (user == null) {
            return;
        } else {
            uid = user.getUid();
        }

        //调用service添加
        favoriteService.setFavorite(rid, uid);
    }

    /**
     * 我的收藏
     *
     * @param currentPage
     * @param pageSize
     * @param session
     * @return
     */
    @RequestMapping("/findFavoriteRoute")
    public PageBean<Route> findFavoriteRoute(String currentPage, String pageSize, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            int currentPage_int = 1;
            if (currentPage != null && currentPage.length() != 0 && !"null".equals(currentPage)) {
                currentPage_int = Integer.parseInt(currentPage);
            }
            int pageSize_int = 8;//如果不赋值，默认为8
            if (pageSize != null && pageSize.length() != 0) {
                pageSize_int = Integer.parseInt(pageSize);
            }

            PageBean<Route> pb = routeService.pageQueryByUser(user, currentPage_int, pageSize_int);
            return pb;
        }
        return null;
    }

    @RequestMapping("/findRouteRank")
    public PageBean<Route> findRouteRank(String currentPage, String pageSize) {
        int currentPage_int = 1;
        if (currentPage != null && currentPage.length() != 0 && !"null".equals(currentPage)) {
            currentPage_int = Integer.parseInt(currentPage);
        }
        int pageSize_int = 8;//如果不赋值，默认为8
        if (pageSize != null && pageSize.length() != 0) {
            pageSize_int = Integer.parseInt(pageSize);
        }
        PageBean<Route> pb = routeService.findRouteRank(currentPage_int, pageSize_int);
        return pb;
    }
}