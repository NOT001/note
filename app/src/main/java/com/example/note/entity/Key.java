package com.example.note.entity;

public class Key {
    private int pass, id;

    public Key(int pass, int id) {
        this.pass = pass;
        this.id = id;
    }

    public int getPass() {
        return pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPass(int pass) {
        this.pass = pass;
    }

    public Key() {
    }
}
