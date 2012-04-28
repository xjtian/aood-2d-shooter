/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.HashMap;

/**
 *
 * @author Jacky Tian
 */
public class ReloadPowerup extends RotatingPowerup {
    private static final HashMap<Integer, BufferedImage> sprites;
    static {
        sprites = new HashMap<Integer, BufferedImage>();
    }
    
    public ReloadPowerup(int x, int y) {
        super(x, y);
        if (sprites.isEmpty())
            loadImages();
    }
    
    public static void loadImages() {
        if (sprites.isEmpty()) {
            BufferedImage original = new BufferedImage(20, 20, BufferedImage.BITMASK);
            Graphics2D g = original.createGraphics();
            g.setColor(Color.yellow);
            g.fillRect(5, 5, 10, 10);

            sprites.put(0, original);

            BufferedImage rotated;
            for (int i = 1; i < 24; i++) {
                rotated = new BufferedImage(20, 20, BufferedImage.BITMASK);
                g = rotated.createGraphics();
                g.rotate(Math.toRadians(i*15), 10, 10);

                g.drawImage(original, 0, 0, null);
                sprites.put(i*15, rotated);
            }
        }
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.drawImage(sprites.get(super.getDirection()), super.getX(), super.getY(), null);
    }

    @Override
    public String getName() {
        return "ammo";
    }

    @Override
    public PowerupType getType() {
        return PowerupType.AMMO;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(getX()+5, getY()+5, 10, 10);
    }

    @Override
    public void drawWithShift(Graphics2D g, int x, int y) {
        g.drawImage(sprites.get(super.getDirection()), super.getX() - x, super.getY() - y, null);
    }

}
