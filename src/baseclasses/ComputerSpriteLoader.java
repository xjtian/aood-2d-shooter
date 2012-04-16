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
 *
 * @author xtian8741
 */
public class ComputerSpriteLoader {
    private static HashMap<Integer, BufferedImage> sprites = new HashMap<Integer, BufferedImage>();
    public static final int WIDTH = 40;
    public static final int HEIGHT = 40;
    
    /**
     * Load all 24 sprites as hardware-accelerated images.
     */
    public static void loadAllImages() {
        int direction = 0;
        BufferedImage loaded;
        while (direction <= 345) {
            try {
                loaded = ImageIO.read(ComputerSpriteLoader.class.getClass().getResource("/resources/playersprites/cs" + Integer.toString(direction) + ".png"));
            } catch (IOException ex) {
                loaded = new BufferedImage(40, 40, BufferedImage.BITMASK);
            }
            
            //Appears that hardware-accelerated image is significantly SLOWER than non-accelerated image on T400.
            //Framerate comparison on school computers reveals regular images are slightly faster
            GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
            BufferedImage image = gc.createCompatibleImage(40, 40, Transparency.BITMASK);
            image.createGraphics().drawImage(loaded, 0, 0, null);
            
            //sprites.put(direction, image);
            sprites.put(direction, loaded);
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
