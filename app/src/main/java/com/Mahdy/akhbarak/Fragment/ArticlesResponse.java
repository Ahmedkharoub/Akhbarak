package com.Mahdy.akhbarak.Fragment;

import com.Mahdy.akhbarak.Article;

import java.util.List;

public class ArticlesResponse {
    private List<Article> articles;
    private int totalItems;

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }
}
