package cn.itcast.domain;

import java.util.List;

public class PageBean<T> {

    private int totalCount; //总记录数 = favoriteCount *
    private int totalPage;  //总页数 = 总记录数 / 每页显示的条数 向上取整
    private int currentPage; // 当前页码 = 前台传递
    private int pageSize; //每页显示的条数 = 前台传递
    private List<T> list; //limit a, b  --a = （当前页码数 - 1） * 每页条数


    public PageBean() {
    }

    public PageBean(int totalCount, int totalPage, int currentPage, int pageSize, List<T> list) {
        this.totalCount = totalCount;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.list = list;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

}
