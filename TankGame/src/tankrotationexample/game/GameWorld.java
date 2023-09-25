package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.Launcher;
import tankrotationexample.Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Tank t1;
    private Tank t2;
    private final Launcher lf;
    private long tick = 0;
    List<GameObject> gameObjects;
    List<Animation> activeAnimations = new ArrayList<>(15);


    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.tick++;
                this.t1.update(this);
                this.t2.update(this);
                this.checkCollisions();
                this.activeAnimations.forEach(a -> a.update());
                this.repaint();
                Thread.sleep(1000 / 144);
                this.activeAnimations.removeIf(a -> !a.isRunning());
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    private void checkCollisions() {
        for (int i = 0; i < this.gameObjects.size(); i++) {
            GameObject obj1 = this.gameObjects.get(i);
            if (obj1 instanceof Wall || obj1 instanceof Health || obj1 instanceof Speed || obj1 instanceof Shield) {
                continue;
            }
            for (int j = 0; j < this.gameObjects.size(); j++) {
                if (i == j) continue;
                GameObject obj2 = this.gameObjects.get(j);

                if (obj2 instanceof Tank) continue;
                if (obj1.getHitBox().intersects(obj2.getHitBox())) {
                    obj1.collides(obj2);
                }}}}

    public void resetGame() {
        this.tick = 0;
        this.t1.setX(300);
        this.t1.setY(300);
    }

    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.GAME_WORLD_WIDTH,
                GameConstants.GAME_WORLD_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        gameObjects = new ArrayList<>(720);
        InputStreamReader isr = new InputStreamReader(GameWorld.class.getClassLoader().getResourceAsStream("maps/map1.csv"));

        try (BufferedReader mapReader = new BufferedReader(isr)) {
            for (int i = 0; mapReader.ready(); i++) {
                String[] items = mapReader.readLine().split(",");
                for (int j = 0; j < items.length; j++) {
                    String objectType = items[j];
                    if ("0".equals(objectType)) continue;
                    if ("Wall".equals(objectType)) {
                        BufferedImage wallImage = Resources.getSprite("wall");
                        gameObjects.add(new Wall(j * 30, i * 30, wallImage));
                    } else {
                        gameObjects.add(GameObject.getNewInstance(j * 30, i * 30, objectType));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading map");
            System.exit(-2);
        }

        t1 = (Tank) GameObject.getNewInstance(200f, 200f, "11");
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);

        t2 = (Tank) GameObject.getNewInstance(1600f, 1200f, "22");
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        this.lf.getJf().addKeyListener(tc2);

        this.gameObjects.add(t1);
        this.gameObjects.add(t2);

        this.activeAnimations.add(new Animation(Resources.getAnimation("bullet"), 100, 200));
        this.activeAnimations.add(new Animation(Resources.getAnimation("nuke"), 200, 200));
        // Uncomment the following lines if you have other active animations
//        this.activeAnimations.add(new Animation(Resources.getAnimation("powerpick"), 300, 200));
//        this.activeAnimations.add(new Animation(Resources.getAnimation("puffsmoke"), 100, 200));
//        this.activeAnimations.add(new Animation(Resources.getAnimation("rocketflame"), 200, 300));
//        this.activeAnimations.add(new Animation(Resources.getAnimation("rockethit"), 300, 200));
    }

    private void drawFloor(Graphics2D buffer) {
        BufferedImage floor = Resources.getSprite("floor");
        for (int i = 0; i < GameConstants.GAME_WORLD_WIDTH; i += 320) {
            for (int j = 0; j < GameConstants.GAME_WORLD_HEIGHT; j += 240) {
                buffer.drawImage(floor, i, j, null);
            }
        }
    }

    private void renderMiniMap(Graphics2D g2, BufferedImage world) {
        BufferedImage mm = world.getSubimage(0, 0, GameConstants.GAME_WORLD_WIDTH, GameConstants.GAME_WORLD_HEIGHT);
        g2.scale(.2, .2);
        g2.drawImage(mm,
                (GameConstants.GAME_SCREEN_WIDTH * 3) / 2 + 300,
                (GameConstants.GAME_SCREEN_HEIGHT * 5) - (GameConstants.GAME_WORLD_HEIGHT + 200), null);
    }

    private int checkBorderX(int x) {
        if (x < GameConstants.GAME_SCREEN_WIDTH / 4) {
            x = GameConstants.GAME_SCREEN_WIDTH / 4;
        } else if (x > GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH / 4) {
            x = GameConstants.GAME_WORLD_WIDTH - GameConstants.GAME_SCREEN_WIDTH / 4;
        }
        x -= GameConstants.GAME_SCREEN_WIDTH / 4;
        return x;
    }

    private int checkBorderY(int y) {
        if (y < GameConstants.GAME_SCREEN_HEIGHT / 2) {
            y = GameConstants.GAME_SCREEN_HEIGHT / 2;
        } else if (y > GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT / 2) {
            y = GameConstants.GAME_WORLD_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT / 2;
        }
        y -= GameConstants.GAME_SCREEN_HEIGHT / 2;
        return y;
    }

    private void renderSplitScreen(Graphics2D g2, BufferedImage world) {
        BufferedImage lh = world.getSubimage(this.checkBorderX((int) this.t1.getX()), this.checkBorderY((int) this.t1.getY()), GameConstants.GAME_SCREEN_WIDTH / 2, GameConstants.GAME_SCREEN_HEIGHT);
        BufferedImage rh = world.getSubimage(this.checkBorderX((int) this.t2.getX()), this.checkBorderY((int) this.t2.getY()), GameConstants.GAME_SCREEN_WIDTH / 2, GameConstants.GAME_SCREEN_HEIGHT);
        g2.drawImage(lh, 0, 0, null);
        g2.drawImage(rh, GameConstants.GAME_SCREEN_WIDTH / 2 + 4, 0, null);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        this.drawFloor(buffer);
        this.gameObjects.forEach(w -> w.drawImage(buffer));
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);
        this.activeAnimations.forEach(a -> a.drawImage(buffer));
        renderSplitScreen(g2, world);
        renderMiniMap(g2, world);
    }
}