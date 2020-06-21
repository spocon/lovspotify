package me.haga.librespot.spotifi.model;

public class Image {



    private int height;
    private int width;
    private Integer size;
    private String key;

    public Image(int height, int width, Integer size, String key) {
        this.height = height;
        this.width = width;
        this.size = size;
        this.key = key;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
