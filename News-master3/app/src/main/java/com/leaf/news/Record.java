package com.leaf.news;

public class Record {
    private String uid;
    private String title;
    private String url;
    public Record(String title, String url){
        setTitle(title);
        setUrl(url);
    }
    public Record(){}
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return uid;
    }

    public void setId(String uid) {
        this.uid = uid;
    }
}
