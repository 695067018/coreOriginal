package com.sug.core.platform.web.pagination.redis;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by A on 2015/1/15.
 */
public class RedisPaginationForm {
    @NotNull
    @Min(0)
    private int pageIndex;

    @NotNull
    @Min(0)
    private int pageSize;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, Object> getQueryMap(){
        Map<String, Object> query = new HashMap<>();
        query.put("startIndex", this.pageIndex == 0? 0: this.pageSize * this.pageIndex);
        query.put("endIndex", this.pageSize);

        return query;
    }
}
