package com.pvz;

public class Goroshstr extends Plant {
    private static final double SHOOT_INTERVAL = 2.0;
    private double shootTimer = 0;

    public Goroshstr(Game game) {
        super("plants/pea1.png", 100, 100, game);
        appendTexture("plants/pea2.png");
        appendTexture("plants/pea3.png");
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        shootTimer += deltaTime;

        if (shootTimer >= SHOOT_INTERVAL && hasZombieInRow()) {
            int rowCenterY = Constant.LAWN_START_Y + (this.row - 1) * Constant.CELL_HEIGHT
                    + (Constant.CELL_HEIGHT / 2) + Constant.GRID_OFFSET_Y;
            int spawnX = (int) (this.x + this.width - 5);
            int spawnY = rowCenterY - 11;

            game.bullets.add(new Gorosh(spawnX, spawnY, game));
            shootTimer = 0;
        }
    }

    private boolean hasZombieInRow() {
        for (Zombie zombie : game.zombies) {
            if (zombie.row == this.row && zombie.isActive) {
                return true;
            }
        }
        return false;
    }
}
