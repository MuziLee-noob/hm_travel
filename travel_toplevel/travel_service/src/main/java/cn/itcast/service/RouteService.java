package cn.itcast.service;

import cn.itcast.domain.PageBean;
import cn.itcast.domain.Route;
import cn.itcast.domain.User;

public interface RouteService {

    PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname);

    Route findOne(String rid);

    PageBean<Route> pageQueryByUser(User user, int currentPage, int pageSize);

    PageBean<Route> findRouteRank(int currentPage, int pageSize);
}
