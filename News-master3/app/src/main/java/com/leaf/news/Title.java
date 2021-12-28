package com.leaf.news;

public class Title {
    private String title;
    private String desc;
    private String imageUrl;
    private String uri;
    private String source;
    private String time;

    public Title(String title, String source, String desc, String imageUrl, String uri, String time) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.desc = desc;
        this.source = source;
        this.uri = uri;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSource() {
        return source;
    }
    public String getDesc() {
        return "";
    }

    public String getUri() {
        return uri;
    }

    public String getTime() {
        return time;
    }
}