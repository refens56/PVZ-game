package com.pvz;

public class Gorosh extends Sprite {
    public int damage = 1;
    private final Game game;

    public Gorosh(int startX, int startY, Game game) {
        super("items/bul.png");
        this.x = startX;
        this.y = startY;
        this.velocityX = 400;
        this.game = game;
        this.width = 24;
        this.height = 22;
    }

    public void ignite() {
        this.damage = 3;
        this.textures.clear();
        appendTexture("items/firebul.png");
        this.textureIndex = 0;
    }

    @Override
    public void update(double deltaTime) {
        x += velocityX * deltaTime;
        if (x > Constant.SCREEN_WIDTH) {
            isActive = false;
            return;
        }

        for (Zombie zombie : game.zombies) {
            if (zombie.isActive && this.collidesWith(zombie)) {
                zombie.hp -= this.damage;
                isActive = false;
                break;
            }
        }
    }
}
