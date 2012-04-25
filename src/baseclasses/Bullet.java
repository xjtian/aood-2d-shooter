/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
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
    
    private double slope;
    private boolean human;
    
    private boolean negativex;
    private boolean negativey;
    
    private final double VEL = 4.0;
    
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
    
    public Bullet(int x, int y, int mx, int my, boolean human) {
        this.x = x;
        this.y = y;
        if (mx - x == 0)
            x--;
        this.slope = Math.abs(((double) (my - y) / (double) (mx - x)));
        negativex = (mx - x) < 0;
        negativey = (my - y) < 0;
        this.human = human;
    }
    
    /**
     * Moves the bullet along its path.
     */
    public void move() {
        double dx = Math.sqrt((VEL*VEL) / (1 + (slope*slope)));
        double dy = slope * dx;
        
        dy = dy + 5*Math.random() - 2.5;
        
        x = (negativex) ? x - (int)dx : x + (int)dx;
        y = (negativey) ? y - (int)dy : y + (int)dy;
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
    
    /**
     * Get point
     * 
     * @return Upper-left of bounding square.
     */
    public Point getPoint() {
        return new Point(x, y);
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, 10, 10);
    }
    
    public Rectangle getShiftedBounds(int offx, int offy) {
        return new Rectangle(x+offx, y+offy, 10, 10);
    }
}
