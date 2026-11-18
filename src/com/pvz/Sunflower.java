package com.pvz;

public class Sunflower extends Plant {
    private static final double SUN_SPAWN_INTERVAL = 15.0;
    private double spawnTimer = 0;

    public Sunflower(Game game) {
        super("plants/sun1.png", 80, 50, game);
        appendTexture("plants/sun2.png");
        appendTexture("plants/sun1.png");
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        spawnTimer += deltaTime;

        if (spawnTimer >= SUN_SPAWN_INTERVAL) {
            game.suns.add(new Sun((int) (this.x + this.width), (int) this.y));
            spawnTimer = 0;
        }
    }
}
