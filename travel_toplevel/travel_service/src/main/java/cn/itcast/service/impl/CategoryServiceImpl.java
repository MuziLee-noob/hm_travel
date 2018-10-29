package cn.itcast.service.impl;

import cn.itcast.dao.CategoryDao;
import cn.itcast.domain.Category;
import cn.itcast.service.CategoryService;
import cn.itcast.util.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDao categoryDao;

    @Override
    public List<Category> findAll() {
        //1.从redis中查询
        Jedis jedis = JedisUtil.getJedis();
        //查询sortedset中的分数和值
        Set<Tuple> categories = jedis.zrangeWithScores("category", 0, -1);
        //2.判断是否为空
        List<Category> list;
        if (categories == null || categories.size() == 0) {
            //3.如果为空，从数据库中查询，存入redis
            list = categoryDao.findAll();
            //将集合数据存储
            for (int i = 0; i < list.size(); i++) {
                jedis.zadd("category", list.get(i).getCid(), list.get(i).getCname());
            }
        } else {
            //将set数据存入list
            list = new ArrayList<>();
            for (Tuple tuple : categories) {
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int)tuple.getScore());
                list.add(category);
            }
        }
        //4.如果不为空，直接返回
        return list;
    }
}
