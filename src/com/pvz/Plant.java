package com.pvz;

public class Plant extends Sprite {
    public int hp;
    public final int cost;
    public int row;
    public int column;
    protected final Game game;

    public Plant(String imagePath, int hp, int cost, Game game) {
        super(imagePath);
        this.hp = hp;
        this.cost = cost;
        this.game = game;
    }

    public void plantAt(int centerX, int centerY, int row, int column) {
        this.x = centerX - (this.width / 2.0);
        this.y = centerY - (this.height / 2.0) + Constant.PLANT_Y_OFFSET;
        this.row = row;
        this.column = column;
    }
}
