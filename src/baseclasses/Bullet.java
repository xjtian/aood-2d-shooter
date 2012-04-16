/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * A class that represents a bullet.
 * 
 * @author Jacky Tian
 */
public class Bullet {
    private int x;
    private int y;
    
    private double angle;
    private boolean human;
    
    private static BufferedImage hsprite;
    private static BufferedImage csprite;
    static {
        try {
            hsprite = ImageIO.read(Bullet.class.getClass().getResource("/resources/playershot.png"));
        } catch (IOException ex) {
            hsprite = new BufferedImage(10, 10, BufferedImage.BITMASK);
        }
        
        try {
            csprite = ImageIO.read(Bullet.class.getClass().getResource("/resources/cpushot.png"));
        } catch (IOException ex) {
            csprite = new BufferedImage(10, 10, BufferedImage.BITMASK);
        }
    }
    
    /**
     * Instantiates a bullet.
     * @param x x-coordinate of shot.
     * @param y y-coordinate of shot.
     * @param angle angle of shot, in radians.
     * @param human whether or not the bullet is yellow.
     */
    public Bullet(int x, int y, double angle, boolean human) {
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.human = human;
    }
    
    /**
     * Moves the bullet along its path.
     */
    public void move() {
        double vmove = Math.sin(angle) * 5;
        double hmove = Math.cos(angle) * 5;
        
        x = (hmove > 1.0 || hmove < -1.0) ? x + (int)hmove : x + 1;
        y = (vmove > 1.0 || vmove < -1.0) ? y - (int)vmove : y + 1;
    }
    
    /**
     * Draw a bullet in the specified graphics context.
     * 
     * @param g Graphics context in which to draw the bullet.
     */
    public void draw(Graphics g) {
        if (human)
            g.drawImage(hsprite, x, y, null);
        else
            g.drawImage(csprite, x, y, null);
    }
    
    /**
     * Get x-coordinate.
     * 
     * @return X-coordinate of the bullet.
     */
    public int getX() {
        return x;
    }
    
    /**
     * Get y-coordinate
     * @return Y-coordinate of the bullet.
     */
    public int getY() {
        return y;
    }
}
