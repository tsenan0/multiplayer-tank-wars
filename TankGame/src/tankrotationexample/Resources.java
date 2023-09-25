package tankrotationexample;



import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class Resources {
    private static final Map<String, BufferedImage> sprites = new HashMap<>();
    private static final Map<String, List<BufferedImage>> animations = new HashMap<>();
    private static final Map<String, Integer> animationInfo = new HashMap<>() {{
        put("bullet", 32);
        put("nuke", 24);
//        put("bullethit", 24);
//        put("bulletshoot", 24);
//        put("powerpick", 32);
//        put("puffsmoke", 32);
//        put("rocketflame", 16);
//        put("rockethit", 32);
    }};


    private static BufferedImage loadSprite(String path) throws IOException {
        return ImageIO.read(Objects.requireNonNull(
                Resources.class.getClassLoader().getResource(path), "Resource %s is not found".formatted(path)));
    }




    private static void initSprites() {
        try {
            Resources.sprites.put("tank1", loadSprite("tank/tank1.png"));
            Resources.sprites.put("tank2", loadSprite("tank/tank2.png"));
            Resources.sprites.put("menu", loadSprite("menu/title.png"));
            Resources.sprites.put("bullet", loadSprite("bullet/bullet.jpg"));
            Resources.sprites.put("rocket1", loadSprite("bullet/rocket1.png"));
            Resources.sprites.put("rocket2", loadSprite("bullet/rocket2.png"));
            Resources.sprites.put("floor", loadSprite("floor/bg.bmp"));
            Resources.sprites.put("wall", loadSprite("walls/unbreak.jpg"));
            Resources.sprites.put("break1", loadSprite("walls/break1.jpg"));
            Resources.sprites.put("break2", loadSprite("walls/break2.jpg"));
            Resources.sprites.put("speed", loadSprite("powerups/speed.png"));
            Resources.sprites.put("health", loadSprite("powerups/health.png"));
            Resources.sprites.put("shield", loadSprite("powerups/shield.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




    public static BufferedImage getSprite(String type) {
        if (!Resources.sprites.containsKey(type)) {
            throw new RuntimeException("Resource %s is missing".formatted(type));
        }
        return Resources.sprites.get(type);
    }

    private static void initAnimations() {
        String animationBasePath = "animations/%s/%s_%04d.png";
        Resources.animationInfo.forEach((animationName, frameCount) -> {
            List<BufferedImage> temp = new ArrayList<>();
            try {
                for (int i = 0; i < frameCount; i++) {
                    String framePath = animationBasePath.formatted(animationName, animationName, i);
                    temp.add(loadSprite(framePath));

                }
                Resources.animations.put(animationName, temp);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public static List<BufferedImage> getAnimation(String type) {
        if (!Resources.animations.containsKey(type)) {
            throw new RuntimeException("Resource %s is missing".formatted(type));
        }
        return Resources.animations.get(type);
    }

    public static void loadAssets() {
        initSprites();

        initAnimations();
    }
}
