package com.pvz;

public class Drevopen extends Plant {
    public Drevopen(Game game) {
        super("plants/tree1.png", 120, 175, game);
        appendTexture("plants/tree2.png");
        appendTexture("plants/tree3.png");
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        for (Gorosh bullet : game.bullets) {
            if (bullet.isActive && this.collidesWith(bullet) && bullet.damage == 1) {
                bullet.ignite();
            }
        }
    }
}
