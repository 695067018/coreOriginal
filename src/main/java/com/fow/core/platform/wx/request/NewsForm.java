package com.fow.core.platform.wx.request;

import java.util.List;

/**
 * Created by Greg.Chen on 2015/8/27.
 */
public class NewsForm {

    public NewsForm(List<Article> articles) {
        this.articles = articles;
    }

    private List<Article> articles;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }
}
