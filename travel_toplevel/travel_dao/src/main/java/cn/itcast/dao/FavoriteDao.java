package cn.itcast.dao;

import cn.itcast.domain.Favorite;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface FavoriteDao {

    /**
     * 查询收藏信息
     * @param rid
     * @param uid
     * @return
     */
    @Select("select * from tab_favorite where rid = #{rid} and uid = #{uid}")
    Favorite findByRidAndUid(@Param("rid") int rid, @Param("uid") int uid);

    @Select("select count(*) from tab_favorite where rid = #{rid}")
    int findCountByRid(int rid);

    /**
     * 设置收藏
     * @param rid
     * @param uid
     */
    @Insert("insert into tab_favorite values (#{rid}, #{date}, #{uid})")
    void setFavorite(@Param("rid") int rid, @Param("date") Date date, @Param("uid") int uid);

    /**
     * 按照收藏次数对线路排序返回分页
     * @return
     */
    @Select("select rid, count(*) count from tab_favorite group by rid order by count desc")
    List<Map<String,Object>> findAllRouteOrderByDesc(int start, int pageSize);

    /**
     * 查找有收藏的总记录数
     * @return
     */
    @Select("select count(*) from (select rid from tab_favorite group by rid) rids")
    int getRouteCount();
}
