package com.jbetfairng.entities;

public class EventType {
    private String id;
    private String name;

    public EventType() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "{" + "" + "id=" + getId() + "," + "name=" + getName() + "}";
    }

}
