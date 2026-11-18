package com.pvz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

    public final List<Plant> plants = new ArrayList<>();
    public final List<Sun> suns = new ArrayList<>();
    public final List<Gorosh> bullets = new ArrayList<>();
    public final List<Zombie> zombies = new ArrayList<>();
    private final List<Cell> occupiedCells = new ArrayList<>();

    private static class Cell {
        final int row;
        final int col;

        Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private int gameState = Constant.STATE_MENU;
    private final Rectangle startButtonBounds = new Rectangle(370, 210, 260, 80);
    private final Rectangle exitButtonBounds = new Rectangle(370, 315, 260, 80);

    private Plant selectedPlant = null;
    private int sunBalance = Constant.INITIAL_SUNS;
    private int deadZombiesCount = 0;
    private double spawnInterval = 25.0;
    public boolean isRunning = true;

    private double spawnTimer = 0;
    private long lastFrameTime = System.nanoTime();
    private final Random random = new Random();

    private final Image mainMenuImg;
    private final Image backgroundImg;
    private final Image menuImg;
    private final Image loseImg;
    private final Image winImg;

    private double scaleX = 1.0;
    private double scaleY = 1.0;

    public Game() {
        setPreferredSize(new Dimension(Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT));
        addMouseListener(this);
        addMouseMotionListener(this);

        mainMenuImg = loadImage("textures/game-menu.png");
        backgroundImg = loadImage("textures/background.jpg");
        menuImg = loadImage("textures/menu_vertical.png");
        loseImg = loadImage("textures/end.png");
        winImg = loadImage("textures/logo.png");

        Timer timer = new Timer(16, this);
        timer.start();
    }

    private Image loadImage(String path) {
        File file = new File(path);
        if (file.exists()) {
            return new ImageIcon(file.getAbsolutePath()).getImage();
        }
        System.out.println("Интерфейс не найден: " + file.getAbsolutePath());
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        long currentTime = System.nanoTime();
        double deltaTime = (currentTime - lastFrameTime) / 1_000_000_000.0;
        lastFrameTime = currentTime;

        if (gameState == Constant.STATE_PLAY && isRunning) {
            updateEntities(deltaTime);
            handleZombieSpawning(deltaTime);

            if (deadZombiesCount >= Constant.ZOMBIES_TO_WIN) {
                isRunning = false;
            }
        }
        repaint();
    }

    private void updateEntities(double deltaTime) {
        for (Plant plant : plants) plant.update(deltaTime);
        plants.removeIf(plant -> !plant.isActive);

        for (Sun sun : suns) sun.update(deltaTime);
        suns.removeIf(sun -> !sun.isActive);

        for (Gorosh bullet : bullets) bullet.update(deltaTime);
        bullets.removeIf(bullet -> !bullet.isActive);

        for (Zombie zombie : zombies) zombie.update(deltaTime);
        zombies.removeIf(zombie -> !zombie.isActive);
    }

    private void handleZombieSpawning(double deltaTime) {
        spawnTimer += deltaTime;
        if (spawnTimer >= spawnInterval && deadZombiesCount < Constant.ZOMBIES_TO_WIN) {
            int randomY = random.nextInt(500 - Constant.LAWN_START_Y) + Constant.LAWN_START_Y;
            int centerY = getCellCenterY(randomY);
            int row = getRowIndex(randomY);
            int zombieType = random.nextInt(3);

            if (zombieType == 0) zombies.add(new DefaultZombie(row, centerY, this));
            else if (zombieType == 1) zombies.add(new ConeZombie(row, centerY, this));
            else zombies.add(new BucketZombie(row, centerY, this));

            spawnTimer = 0;
        }
    }

    public void onZombieKilled() {
        deadZombiesCount++;
        spawnInterval = Math.max(5.0, spawnInterval - 1.0);
    }

    public void freeCell(int row, int col) {
        occupiedCells.removeIf(cell -> cell.row == row && cell.col == col);
    }

    private boolean isCellOccupied(int row, int col) {
        for (Cell cell : occupiedCells) {
            if (cell.row == row && cell.col == col) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        scaleX = (double) getWidth() / Constant.SCREEN_WIDTH;
        scaleY = (double) getHeight() / Constant.SCREEN_HEIGHT;
        g2d.scale(scaleX, scaleY);

        if (gameState == Constant.STATE_MENU) {
            drawMainMenu(g);
        } else {
            drawGameplay(g);
        }
    }

    private void drawMainMenu(Graphics g) {
        if (mainMenuImg != null) {
            g.drawImage(mainMenuImg, 0, 0, Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT, null);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.fillRect(0, 0, Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT);
        }
    }

    private void drawGameplay(Graphics g) {
        if (backgroundImg != null) g.drawImage(backgroundImg, 0, 0, Constant.SCREEN_WIDTH, Constant.SCREEN_HEIGHT, null);
        if (menuImg != null) g.drawImage(menuImg, 0, 0, 134, Constant.SCREEN_HEIGHT, null);

        g.setColor(new Color(255, 1, 255));
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(String.valueOf(sunBalance), 25, 110);

        for (Plant plant : plants) plant.draw(g);
        for (Sun sun : suns) sun.draw(g);
        for (Gorosh bullet : bullets) bullet.draw(g);
        for (Zombie zombie : zombies) zombie.draw(g);

        if (selectedPlant != null) {
            selectedPlant.draw(g);
        }

        if (!isRunning) {
            Image endScreen = (deadZombiesCount >= Constant.ZOMBIES_TO_WIN) ? winImg : loseImg;
            if (endScreen != null) {
                g.drawImage(endScreen, Constant.SCREEN_WIDTH / 2 - 250, Constant.SCREEN_HEIGHT / 2 - 200, 500, 400, null);
            }
        }
    }

    private int getColumnIndex(int x) {
        return (x - Constant.LAWN_START_X) / Constant.CELL_WIDTH + 1;
    }

    private int getRowIndex(int y) {
        return (y - Constant.LAWN_START_Y) / Constant.CELL_HEIGHT + 1;
    }

    private int getCellCenterX(int x) {
        int col = getColumnIndex(x);
        return Constant.LAWN_START_X + (col - 1) * Constant.CELL_WIDTH + (Constant.CELL_WIDTH / 2) + Constant.GRID_OFFSET_X;
    }

    private int getCellCenterY(int y) {
        int row = getRowIndex(y);
        return Constant.LAWN_START_Y + (row - 1) * Constant.CELL_HEIGHT + (Constant.CELL_HEIGHT / 2) + Constant.GRID_OFFSET_Y;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x = (int) (e.getX() / scaleX);
        int y = (int) (e.getY() / scaleY);


        if (gameState == Constant.STATE_MENU) {
            if (startButtonBounds.contains(x, y)) {
                gameState = Constant.STATE_PLAY;
                lastFrameTime = System.nanoTime();
            } else if (exitButtonBounds.contains(x, y)) {
                System.exit(0);
            }
            return;
        }

        if (x > 14 && x < 115) {
            if (y > 118 && y < 225) selectedPlant = new Sunflower(this);
            else if (y > 225 && y < 332) selectedPlant = new Goroshstr(this);
            else if (y > 332 && y < 439) selectedPlant = new Nut(this);
            else if (y > 439 && y < 546) selectedPlant = new Drevopen(this);

            if (selectedPlant != null) {
                selectedPlant.width = 75;
                selectedPlant.height = 75;
                selectedPlant.x = x - selectedPlant.width / 2.0;
                selectedPlant.y = y - selectedPlant.height / 2.0;
            }
        }

        for (Sun sun : suns) {
            if (sun.isActive && sun.getBounds().contains(x, y)) {
                sun.isActive = false;
                sunBalance += Constant.SUN_VALUE;
                break;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (gameState != Constant.STATE_PLAY || selectedPlant == null) return;

        int x = (int) (e.getX() / scaleX);
        int y = (int) (e.getY() / scaleY);

        boolean insideLawn = x >= Constant.LAWN_START_X && x <= Constant.LAWN_END_X
                && y >= Constant.LAWN_START_Y && y <= Constant.LAWN_END_Y;

        if (insideLawn) {
            int row = getRowIndex(y);
            int col = getColumnIndex(x);

            if (!isCellOccupied(row, col) && sunBalance >= selectedPlant.cost) {
                occupiedCells.add(new Cell(row, col));
                sunBalance -= selectedPlant.cost;
                selectedPlant.plantAt(getCellCenterX(x), getCellCenterY(y), row, col);
                plants.add(selectedPlant);
            }
        }
        selectedPlant = null;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameState == Constant.STATE_PLAY && selectedPlant != null) {
            selectedPlant.x = (int) (e.getX() / scaleX) - selectedPlant.width / 2.0;
            selectedPlant.y = (int) (e.getY() / scaleY) - selectedPlant.height / 2.0;
        }
    }

    @Override public void mouseDragged(MouseEvent e) { mouseMoved(e); }
    @Override public void mouseClicked(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame(Constant.SCREEN_TITLE);
        Game gamePanel = new Game();
        frame.add(gamePanel);
        frame.setUndecorated(true);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(frame);
        } else {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        }
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        });
    }
}