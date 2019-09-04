package com.example.gj.puzzle.entity;

/**
 * Created by gj
 * Created on 8/25/19
 * Description
 */

public class Player {
    private int id;
    private String name;
    private String score;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
