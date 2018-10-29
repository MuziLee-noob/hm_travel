package cn.itcast.dao;

import cn.itcast.dao.provider.DynamicSqlProvider;
import cn.itcast.domain.Route;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RouteDao {

    /**
     * 根据条件查询route信息
     * @param rname
     * @param cid
     * @return
     */
    @SelectProvider(type = DynamicSqlProvider.class, method = "findAll")
    List<Route> findAll(@Param("rname") String rname, @Param("cid") int cid);

    /**
     * 查找一条线路的详细信息
     * @param rid
     * @return
     */
    @Select("select * from tab_route where rid = #{rid}")
    Route findOne(int rid);

    /**
     * 查找用户收藏信息
     * @param uid
     * @return
     */
    @Select("SELECT * FROM tab_route WHERE rid IN( SELECT rid FROM tab_favorite WHERE uid = #{uid})")
    List<Route> findUserFavorite(int uid);

}
