package com.pvz;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Sprite {
    public double x;
    public double y;
    public double velocityX;
    public double velocityY;
    public int width;
    public int height;
    public boolean isActive = true;

    protected final List<Image> textures = new ArrayList<>();
    protected int textureIndex = 0;
    protected double animationTimer = 0;
    protected double animationInterval = 0.09;

    public Sprite(String imagePath) {
        appendTexture(imagePath);
        this.width = 75;
        this.height = 75;
    }

    public void appendTexture(String imagePath) {
        File file = new File(imagePath);
        if (file.exists()) {
            textures.add(new ImageIcon(file.getAbsolutePath()).getImage());
        } else {
            System.out.println("Текстура не найдена: " + file.getAbsolutePath());
        }
    }

    public void update(double deltaTime) {
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;
        updateAnimation(deltaTime);
    }

    protected void updateAnimation(double deltaTime) {
        if (textures.size() <= 1) return;

        animationTimer += deltaTime;
        if (animationTimer >= animationInterval) {
            animationTimer = 0;
            textureIndex = (textureIndex + 1) % textures.size();
        }
    }

    public void draw(Graphics g) {
        if (!textures.isEmpty() && textureIndex < textures.size()) {
            g.drawImage(textures.get(textureIndex), (int) x, (int) y, width, height, null);
        } else {
            g.setColor(Color.GREEN);
            g.fillRect((int) x, (int) y, width, height);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public boolean collidesWith(Sprite other) {
        return this.getBounds().intersects(other.getBounds());
    }
}
