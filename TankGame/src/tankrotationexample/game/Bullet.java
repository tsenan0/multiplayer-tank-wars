package tankrotationexample.game;

import tankrotationexample.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject{
    private float x;
    private float y;
    private float vx;
    private float vy;
    private float charge = 1f;
    private float angle;
    private float R = 2;
    private BufferedImage img;
    public Rectangle hitbox;

    Bullet(float x, float y, BufferedImage img, float angle) {
        this.x = x;
        this.y = y;
        this.img = img;
        this.vx = 0;
        this.vy = 0;
        this.angle = angle;
        this.hitbox = new Rectangle((int)x, (int)y, this.img.getWidth(), this.img.getHeight());
    }
@Override
    public Rectangle getHitBox() {
        return this.hitbox.getBounds();
    }

    public void increaseCharge() {
        this.charge = this.charge + 0.05f;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }
    void update() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitbox.setLocation((int)x, (int)y);
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
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }
    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        rotation.scale(this.charge, this.charge);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        //g2d.setColor(Color.BLUE);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        //g2d.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());
    }


    public void collides(GameObject obj2) {

    }

    public void setHeading(float x, float y, float angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }
}
