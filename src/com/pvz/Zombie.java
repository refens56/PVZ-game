package com.pvz;

public class Zombie extends Sprite {
    public int hp;
    public final int row;
    public boolean isEating = false;
    protected final Game game;

    public Zombie(String imagePath, int hp, int row, int centerY, Game game) {
        super(imagePath);
        this.hp = hp;
        this.row = row;
        this.x = Constant.SCREEN_WIDTH;
        this.width = 62;
        this.height = 80;
        this.y = centerY - (this.height / 2.0);
        this.velocityX = -15;
        this.game = game;
    }

    @Override
    public void update(double deltaTime) {
        if (hp <= 0) {
            isActive = false;
            game.onZombieKilled();
            return;
        }

        isEating = false;
        for (Plant plant : game.plants) {
            if (plant.isActive && this.collidesWith(plant) && plant.row == this.row) {
                isEating = true;
                plant.hp -= 20 * deltaTime;
                if (plant.hp <= 0) {
                    plant.isActive = false;
                    game.freeCell(plant.row, plant.column);
                }
                break;
            }
        }

        if (!isEating) {
            x += velocityX * deltaTime;
        }

        if (x <= Constant.LAWN_START_X) {
            game.isRunning = false;
        }

        updateAnimation(deltaTime);
    }
}
