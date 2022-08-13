package com.dangdang.entity;

import java.util.List;

public class BookList {

    private String currentDate;

    private Integer hasNextPage;

    private List<DMedia> mediaList;

    private String systemDate;

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public Integer getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(Integer hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public List<DMedia> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<DMedia> mediaList) {
        this.mediaList = mediaList;
    }

    public String getSystemDate() {
        return systemDate;
    }

    public void setSystemDate(String systemDate) {
        this.systemDate = systemDate;
    }
}
