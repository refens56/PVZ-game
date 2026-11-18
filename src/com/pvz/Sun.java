package com.pvz;

public class Sun extends Sprite {
    private static final double MAX_LIFETIME = 8.0;
    private double lifetime = 0;

    public Sun(int startX, int startY) {
        super("items/sun.png");
        this.x = startX;
        this.y = startY;
        this.width = 35;
        this.height = 35;
    }

    @Override
    public void update(double deltaTime) {
        lifetime += deltaTime;
        if (lifetime >= MAX_LIFETIME) {
            isActive = false;
        }
    }
}
