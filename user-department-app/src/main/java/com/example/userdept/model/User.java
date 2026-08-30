package com.example.userdept.model;

import java.util.concurrent.atomic.AtomicLong;

public class User {

    private static final AtomicLong ID_SEQUENCE = new AtomicLong(1);

    private final long id;
    private final String name;
    private final int departmentId;
    private boolean active;

    public User(String name, int departmentId) {
        this.id = ID_SEQUENCE.getAndIncrement();
        this.name = name;
        this.departmentId = departmentId;
        this.active = true;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
