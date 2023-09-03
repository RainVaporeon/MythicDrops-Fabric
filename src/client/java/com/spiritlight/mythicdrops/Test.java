package com.spiritlight.mythicdrops;

public class Test {

    public static void main(String[] args) {
        try {
            System.out.println(Internals.loadItems().size());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
