package com.example.myfirstapp.model;

public class User {

    int user_id;
    String username;
    String email;
    String created_at;
    String updated_at;

    public User(int user_id, String username, String email, String created_at, String updated_at) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public User(int user_id, String username, String created_at, String updated_at) {
        this.user_id = user_id;
        this.username = username;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public User(int user_id, String created_at, String updated_at) {
        this.user_id = user_id;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
