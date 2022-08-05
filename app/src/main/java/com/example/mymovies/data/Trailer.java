package com.example.mymovies.data;

public class Trailer {
    private final String kay;
    private String name;

    public Trailer(String kay, String name) {
        this.kay = kay;
        this.name = name;
    }

    public String getKay() {
        return kay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
