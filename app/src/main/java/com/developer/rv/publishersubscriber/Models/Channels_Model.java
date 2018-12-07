package com.developer.rv.publishersubscriber.Models;

public class Channels_Model {

    private String pub_email,
                   name,
                   subject,
                   topic;

    public Channels_Model() {
    }

    public Channels_Model(String subject, String topic) {
        this.subject = subject;
        this.topic = topic;
    }

    public Channels_Model(String name, String subject, String topic) {
        this.name = name;
        this.subject = subject;
        this.topic = topic;
    }

    public String getPub_email() {
        return pub_email;
    }

    public void setPub_email(String pub_email) {
        this.pub_email = pub_email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
