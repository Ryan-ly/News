package com.leaf.news.util;

import com.google.gson.Gson;
import com.leaf.news.NewsList;

public class Utility {
    public static NewsList parseJsonWithGson(final String requestText){
        Gson gson = new Gson();
        return gson.fromJson(requestText, NewsList.class); // 从Json相关对象到Java实体的方法
    }

}