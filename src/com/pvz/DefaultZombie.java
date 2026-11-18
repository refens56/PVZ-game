package com.pvz;

public class DefaultZombie extends Zombie {
    public DefaultZombie(int row, int centerY, Game game) {
        super("zombies/zom1.png", 12, row, centerY, game);
        appendTexture("zombies/zom2.png");
    }
}
