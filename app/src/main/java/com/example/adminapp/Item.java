package com.example.adminapp;

public class Item {
    String name;
    String email;
    int score;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Item(String name, String email, int score) {
        this.name = name;
        this.email = email;
        this.score = score;
    }


}
