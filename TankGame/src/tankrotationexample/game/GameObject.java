package tankrotationexample.game;



import tankrotationexample.Resources;

import java.awt.*;


public abstract class GameObject {
    public Rectangle hitbox;

    public static GameObject getNewInstance(float x, float y, String type) {
        return switch (type) {
            case "9", "3" -> new Wall(x, y, Resources.getSprite("wall"));
            case "2" -> new BreakableWall(x, y, Resources.getSprite("break1"));
            case "4" -> new Speed(x, y, Resources.getSprite("speed"));
            case "5" -> new Health(x, y, Resources.getSprite("health"));
            case "6" -> new Shield(x, y, Resources.getSprite("shield"));
            case "11" -> new Tank(x, y, Resources.getSprite("tank1"));
            case "22" -> new Tank(x, y, Resources.getSprite("tank2"));
            default -> throw new IllegalArgumentException("%s type not supported".formatted(type));
        };
    }

    public abstract void drawImage(Graphics g);

    public abstract Rectangle getHitBox();

    public abstract void collides(GameObject obj2);


}