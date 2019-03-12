package com.sug.core.platform.web.pagination;

import com.sug.core.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by A on 2015/1/15.
 */
public class PaginationForm {
    private final Logger logger = LoggerFactory.getLogger(PaginationForm.class);


    private Integer pageIndex;


    private Integer pageSize;

    private List<String> sorting;

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Map<String, Object> getQueryMap(){
        Map<String, Object> query = new HashMap<>();

        Field[] fields = this.getClass().getDeclaredFields();

        if(pageSize != null){
            query.put("startIndex",this.pageSize * (this.pageIndex == null ? 0 : this.pageIndex));
            query.put("pageSize",this.pageSize);
        }
        if(sorting != null)
            query.put("sorting", sorting);

        try {
            for(Field field : fields){
                field.setAccessible(true);
                if(field.getType().equals(String.class) && !StringUtils.hasText((String) field.get(this)))
                    continue;
                else if(field.get(this) == null)
                    continue;
                query.put(field.getName(),field.get(this));
            }
        } catch (IllegalAccessException e) {
            logger.warn(e.getMessage());
        }

        return query;
    }

    public List<String> getSorting() {
        return sorting;
    }

    public void setSorting(List<String> sorting) {
        this.sorting = sorting;
    }
}
