package com.example.rig.models;

public class User {

    private String id;
    private String email;
    private String password;
    private String initial;
    private String name;
    private String role;
    private String ban;

    public User() {}

    public User(String id, String email, String password, String initial, String name, String role, String ban) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.initial = initial;
        this.name = name;
        this.role = role;
        this.ban = ban;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBan() {
        return ban;
    }

    public void setBan(String ban) {
        this.ban = ban;
    }
}
