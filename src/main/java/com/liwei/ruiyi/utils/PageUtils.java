package com.liwei.ruiyi.utils;

import com.alibaba.fastjson2.JSONObject;

import java.util.List;

public class PageUtils {
    private int page = 0;
    private int limit = 0;
    private int pageSize = 0;
    private List data;
    private int total;
    private int pageStart;

    private JSONObject searchParams;

    public JSONObject getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(JSONObject searchParams) {
        this.searchParams = searchParams;
    }

    public int getPageStart() {
        return (page-1)*limit;
    }

    public void setPageStart(int pageStart) {
        this.pageStart = pageStart;
    }

    public PageUtils(int page, int limit) {
        this.page = page;
        this.limit = limit;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPageSize() {
        if (data != null) {
            pageSize = 0;
        } else {
            pageSize = (int) Math.ceil(data.size() / limit);
        }
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
