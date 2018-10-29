package cn.itcast.service.impl;

import cn.itcast.dao.FavoriteDao;
import cn.itcast.domain.Favorite;
import cn.itcast.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteDao favoriteDao;

    /**
     * 判断当前用户是否收藏
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public boolean isFavorite(String rid, int uid) {

        Favorite favorite = favoriteDao.findByRidAndUid(Integer.parseInt(rid), uid);

        return favorite != null;
    }

    /**
     * 设置收藏
     * @param rid
     * @param uid
     */
    @Override
    public void setFavorite(String rid, int uid) {
        Date date = new Date();

        favoriteDao.setFavorite(Integer.parseInt(rid), date, uid);
    }
}
