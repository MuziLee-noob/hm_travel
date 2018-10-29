package cn.itcast.service.impl;

import cn.itcast.dao.FavoriteDao;
import cn.itcast.dao.RouteDao;
import cn.itcast.dao.RouteImgDao;
import cn.itcast.dao.SellerDao;
import cn.itcast.domain.*;
import cn.itcast.service.RouteService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RouteServiceImpl implements RouteService {

    @Autowired
    private RouteDao routeDao;
    @Autowired
    private RouteImgDao routeImgDao;
    @Autowired
    private SellerDao sellerDao;
    @Autowired
    private FavoriteDao favoriteDao;

    /**
     * 根据条件查询线路列表
     * @param cid
     * @param currentPage
     * @param pageSize
     * @param rname
     * @return
     */
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        PageBean<Route> pb = new PageBean<>();
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);

        Page<Route> page = PageHelper.startPage(currentPage, pageSize);
        List<Route> routeList = routeDao.findAll(rname, cid);
        List<Route> result = page.getResult();

        pb.setList(result);
        pb.setTotalCount((int) page.getTotal());
        pb.setTotalPage(page.getPages());
        return pb;
    }

    /**
     * 根据rid查询线路详细信息
     * @param rid
     * @return
     */
    @Override
    public Route findOne(String rid) {
        //1.查询route对象
        Route route = routeDao.findOne(Integer.parseInt(rid));
        //2.查询图片集合
        List<RouteImg> list = routeImgDao.findById(route.getRid());
        route.setRouteImgList(list);
        //3.查询商家信息
        Seller seller = sellerDao.findById(route.getSid());
        route.setSeller(seller);
        //4.查询收藏次数
        int count = favoriteDao.findCountByRid(Integer.parseInt(rid));
        route.setCount(count);

        return route;
    }

    /**
     * 查询用户收藏线路信息
     * @param user
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<Route> pageQueryByUser(User user, int currentPage, int pageSize) {

        PageBean<Route> pb = new PageBean<>();

        Page<Route> page = PageHelper.startPage(currentPage, pageSize);
        List<Route> routes = routeDao.findUserFavorite(user.getUid());

        pb.setTotalPage(page.getPages());
        pb.setTotalCount((int) page.getTotal());
        pb.setList(page.getResult());
        pb.setPageSize(pageSize);
        pb.setCurrentPage(currentPage);

        return pb;
    }

    /**
     * 收藏排行榜
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageBean<Route> findRouteRank(int currentPage, int pageSize) {
        PageBean<Route> pb = new PageBean<>();
        //1.查找排序后的线路集合（rid + 收藏次数）
        int start = (currentPage - 1) * pageSize;
        List<Map<String, Object>> list = favoriteDao.findAllRouteOrderByDesc(start, pageSize);
        int end;
        if (start + 7 >= list.size()) {
            end = list.size() - 1;
        } else {
            end = start + 7;
        }

        //2.根据rid查找线路封装route对象，装入集合中
        List<Route> routeList = new ArrayList<>();
        for (int i = start; i <= end; i++) {
            /*for (Map<String, Object> map : list) {*/
            int rid = (int) list.get(i).get("rid");
            Route route = routeDao.findOne(rid);
            int count = Integer.parseInt(String.valueOf((long) list.get(i).get("count"))) ;
            route.setCount(count);
            route.setRank(i + 1);
            routeList.add(route);
        }

        pb.setList(routeList);

        //3.设置当前页和pageSize
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        //4.设置totalCount和totalPage
        int totalCount = favoriteDao.getRouteCount();
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        pb.setTotalCount(totalCount);
        pb.setTotalPage(totalPage);

        return pb;
    }
}
