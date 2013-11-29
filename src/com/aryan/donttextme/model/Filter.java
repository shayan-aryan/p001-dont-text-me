package com.aryan.donttextme.model;

/**
 * Created by Shayan on 11/29/13.
 */
public class Filter {
    private String name;
    private String filterKey;

    public void setName(String name) {
        this.name = name;
    }

    public Filter(String filterKey, String name) {
        this.name = name;
        this.filterKey = filterKey;
    }

    public void setFilterKey(String filterKey) {
        this.filterKey = filterKey;
    }

    public String getName() {
        return name;
    }

    public String getFilterKey() {
        return filterKey;
    }
}
