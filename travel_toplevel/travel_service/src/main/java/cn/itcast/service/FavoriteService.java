package cn.itcast.service;

public interface FavoriteService {

    /**
     * 判断是否收藏
     * @param rid
     * @param uid
     * @return
     */
    boolean isFavorite(String rid, int uid);

    /**
     * 设置收藏
     * @param rid
     * @param uid
     */
    void setFavorite(String rid, int uid);
}
