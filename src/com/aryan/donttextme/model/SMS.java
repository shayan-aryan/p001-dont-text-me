package com.aryan.donttextme.model;

/**
 * Created by Shayan on 11/20/13.
 */
public class SMS {

    private long time;
    private String sender;
    private String body;
    private boolean unread;
    private int filterKey;

    public SMS(long time, String sender, String body, boolean unread, int filterKey) {
        this.time = time;
        this.sender = sender;
        this.body = body;
        this.unread = unread;
        this.filterKey = filterKey;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setUnread(boolean unread) {
        this.unread = unread;
    }

    public void setFilterKey(int filterKey) {
        this.filterKey = filterKey;
    }

    public long getTime() {
        return time;
    }

    public String getSender() {
        return sender;
    }

    public String getBody() {
        return body;
    }

    public boolean isUnread() {
        return unread;
    }

    public int getFilterKey() {
        return filterKey;
    }
}
