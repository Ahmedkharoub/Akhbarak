package com.Mahdy.akhbarak;

public class Article {

    long id;
    String title;
    String body;
    String updatedAt;
    String imageUrl;
    String name;
    String titleAr;
    String bodyAr;
    Boolean isEnglish;

    public String getTitleAr() {
        return titleAr;
    }

    public void setTitleAr(String titleAr) {
        this.titleAr = titleAr;
    }

    public String getBodyAr() {
        return bodyAr;
    }

    public void setBodyAr(String bodyAr) {
        this.bodyAr = bodyAr;
    }

    public Article(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getEnglish() {
        return isEnglish;
    }

    public void setEnglish(Boolean english) {
        isEnglish = english;
    }
}
