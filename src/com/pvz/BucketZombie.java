package com.pvz;

public class BucketZombie extends Zombie {
    public BucketZombie(int row, int centerY, Game game) {
        super("zombies/buck1.png", 22, row, centerY, game);
        appendTexture("zombies/buck2.png");
    }
}
