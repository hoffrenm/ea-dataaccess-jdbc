package org.example.Models;

public class CustomerGenre {

    private String name;
    private int count;

    public CustomerGenre(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "CustomerGenre{" +
                "genre name='" + name + '\'' +
                ", count of tracks='" + count + '\'' +
                '}';
    }
}