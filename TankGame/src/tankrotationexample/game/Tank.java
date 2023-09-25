package tankrotationexample.game;

import tankrotationexample.GameConstants;
import tankrotationexample.Resources;


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObject {

    private float x;
    private float y;
    private float screen_x;
    private float screen_y;
    List<Bullet> ammo = new ArrayList<>();
    long timeSinceLastShot = 0L;
    long cooldown = 2000;
    Bullet currentChargeBullet = null;
    private float vx;
    private float vy;
    private float angle;

    private float R = 5;
    private float ROTATIONSPEED = 3.0f;

    private BufferedImage img;
    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean shootPressed;
    public Rectangle hitbox;
    Tank(float x, float y, BufferedImage img) {
        this.x = x;
        this.y = y;
        this.vx = 0;
        this.vy = 0;
        this.img = img;
        this.angle = 0;
        this.hitbox = new Rectangle((int)x, (int)y, this.img.getWidth(), this.img.getHeight());
    }
@Override
    public Rectangle getHitBox() {
        return this.hitbox.getBounds();
    }

    public float getScreen_x() {
        return screen_x;
    }

    public float getScreen_y() {
        return screen_y;
    }
    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }
    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    // Space for shooting

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void update(GameWorld gw) {
        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }

        if (this.shootPressed && ((this.timeSinceLastShot + this.cooldown) < System.currentTimeMillis())) {
          //  Resources.getSound("bullet").play();
            this.ammo.add(new Bullet(this.safeShootX(), this.safeShootY(), Resources.getSprite("bullet"), angle));
            Animation a = new Animation(Resources.getAnimation("bullet"),safeShootX() ,safeShootY());
            gw.activeAnimations.add(a);
          //  Resources.getSound("fire").play();
            this.timeSinceLastShot = System.currentTimeMillis();

            /* if(this.currentChargeBullet == null) {
                this.currentChargeBullet = new Bullet(safeShootX(), safeShootY(), Resources.getSprite("bullet"), angle);
                Resources.getSound("fire").play();
                Animation a = new Animation(Resources.getAnimation("bullet"),safeShootX() ,safeShootY());
                gw.activeAnimations.add(a);
            } else {
                this.currentChargeBullet.increaseCharge();
                this.currentChargeBullet.setHeading(x, y, angle);
            }
        } else {
            if(this.currentChargeBullet != null) {
                this.ammo.add(this.currentChargeBullet);
                this.timeSinceLastShot = System.currentTimeMillis();
                this.currentChargeBullet = null;
            } */
        }

        this.ammo.forEach(bullet -> bullet.update());
        this.hitbox.setLocation((int)x, (int)y);

    }

    private int safeShootX() {
        double cx =31 * Math.cos(Math.toRadians(this.angle));
        return(int)(x+this.img.getWidth()/2f+cx-4f);
    }
    private int safeShootY() {
        double cy=31* Math.sin(Math.toRadians(this.angle));
        return (int)(y+this.img.getWidth()/2f+cy-4f);

    }


    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx =  Math.round(R * Math.cos(Math.toRadians(angle)));
        vy =  Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
        checkBorder();
        centerScreen();
    }


    private void moveForwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        centerScreen();
    }

    private void centerScreen() {
        this.screen_x = this.x - GameConstants.GAME_SCREEN_WIDTH / 2;
        this.screen_y = this.y - GameConstants.GAME_SCREEN_HEIGHT / 2;
    }


    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.GAME_WORLD_WIDTH - 88) {
            x = GameConstants.GAME_WORLD_WIDTH - 88;
        }
        if (y < 40) {
            y = 40;
        }
        if (y >= GameConstants.GAME_WORLD_HEIGHT - 80) {
            y = GameConstants.GAME_WORLD_HEIGHT - 80;
        }
    }

    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        this.ammo.forEach(b -> b.drawImage(g2d));

        if(this.currentChargeBullet != null) {
            this.currentChargeBullet.drawImage(g2d);
        }

        g2d.setColor(Color.ORANGE);
        g2d.drawRect((int)x, (int)y - 20, 100, 15);
        long currentWidth = 100 - ((this.timeSinceLastShot + this.cooldown) - System.currentTimeMillis()) / 40;
        if (currentWidth > 100) {
            currentWidth = 100;
        }
        g2d.fillRect((int)x, (int)y - 20, (int)currentWidth, 15);
    }



    public void collides(GameObject with) {
        if(with instanceof Bullet) {
            //lose life
        } else if (with instanceof Wall) {
            //stop
        } else if (with instanceof PowerUp) {
            ((PowerUp)with).applyPower(this);
        }

    }

    public void toggleShootPressed() {
        this.shootPressed = true;
    }

    public void unToggleShootPressed() {
        this.shootPressed = false;
    }
}
