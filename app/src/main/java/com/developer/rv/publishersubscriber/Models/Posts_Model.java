package com.developer.rv.publishersubscriber.Models;

public class Posts_Model {

    String name ,
           content;

    public Posts_Model() {
    }

    public Posts_Model(String content) {
        this.content = content;
    }

    public Posts_Model(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
