package tankrotationexample.game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BreakableWall extends Wall {
    float x, y;
    BufferedImage img;
    private Rectangle hitbox;

    BreakableWall(float x, float y, BufferedImage img){
        this.x=x;
        this.y =y;
        this.img = img;
        this.hitbox = new Rectangle((int) x,(int) y, this.img.getWidth(), this.img.getHeight());
    }
    public Rectangle getHitBox() {
        return this.hitbox.getBounds();
    }
    public void drawImage(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, (int) x, (int) y, null);

    }

}