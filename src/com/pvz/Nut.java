package com.pvz;

public class Nut extends Plant {
    public Nut(Game game) {
        super("plants/nut1.png", 200, 50, game);
        appendTexture("plants/nut2.png");
        appendTexture("plants/nut3.png");
    }
}
