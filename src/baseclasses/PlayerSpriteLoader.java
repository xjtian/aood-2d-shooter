/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * This is a class that contains every player sprite based on turn direction in degrees 
 * as a hardware-accelerated image. Before doing anything in the applet/game, call 
 * <code>loadAllImages()</code> to populate the static map of sprites so that any 
 * repainting and rendering done in the window uses GPU.
 * 
 * @author Jacky Tian
 */
public class PlayerSpriteLoader {
    private static HashMap<Integer, BufferedImage> sprites = new HashMap<Integer, BufferedImage>();
    private static final int WIDTH = 40;
    private static final int HEIGHT = 40;
    
    /**
     * Load all 24 sprites as hardware-accelerated images.
     */
    public static void loadAllImages() {
        int direction = 0;
        BufferedImage loaded;
        while (direction <= 345) {
            try {
                loaded = ImageIO.read(PlayerSpriteLoader.class.getClass().getResource("/resources/playersprites/ps" + Integer.toString(direction) + ".png"));
            } catch (IOException ex) {
                loaded = new BufferedImage(40, 40, BufferedImage.TYPE_INT_RGB);
            }
            
            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(40, 40, Transparency.BITMASK);
            image.createGraphics().drawImage(loaded, 0, 0, null);
            
            sprites.put(direction, image);
            direction += 15;
        }
    }
    
    /**
     * Gets the sprite for a certain degree of turn.
     * 
     * @param direction The degree measure of turn, in 15-degree increments up to 345.
     * @return The corresponding sprite, if the degree value is valid.
     */
    public static BufferedImage getSprite(int direction) {
        return sprites.get(direction);
    }
}
