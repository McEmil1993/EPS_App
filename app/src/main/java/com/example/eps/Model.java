package com.example.eps;

import android.content.Context;

public class Model {
    private String title;
    private String content;
     Context context;


    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
