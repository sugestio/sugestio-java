package com.sugestio.client.model.detail;


public class ThumbRating extends Detail {

    private Type type;

    public enum Type {
        UP,
        DOWN
    }

    public ThumbRating(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "THUMB:" + type.name();
    }
}
