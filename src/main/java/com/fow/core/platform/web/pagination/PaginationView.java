package com.fow.core.platform.web.pagination;

import java.util.List;

/**
 * Created by Greg.Chen on 2015/1/15.
 */
public class PaginationView<T> {
    private List<T> list;

    private int count;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
