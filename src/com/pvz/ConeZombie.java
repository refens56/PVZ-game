package com.pvz;

public class ConeZombie extends Zombie {
    public ConeZombie(int row, int centerY, Game game) {
        super("zombies/cone1.png", 20, row, centerY, game);
        appendTexture("zombies/cone2.png");
    }
}
