/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
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
    
    private final double VEL = 5.0;
    
    private static BufferedImage hsprite;
    private static BufferedImage csprite;
    static {
        try {
            hsprite = ImageIO.read(Bullet.class.getResource("/resources/playershot.png"));
        } catch (Exception ex) {
            hsprite = new BufferedImage(10, 10, BufferedImage.BITMASK);
        }
        
        try {
            csprite = ImageIO.read(Bullet.class.getResource("/resources/cpushot.png"));
        } catch (Exception ex) {
            csprite = new BufferedImage(10, 10, BufferedImage.BITMASK);
        }
    }
    
    /**
     * Creates a new bullet and a linear flight path.
     * 
     * @param x x-coordinate of the bullet.
     * @param y y-coordinate of the bullet.
     * @param mx x-coordinate of second point for slope of bullet path.
     * @param my y-coordinate of second point for slope of bullet path.
     * @param human true if bullet is fired by a human player.
     */
    public Bullet(int x, int y, int mx, int my, boolean human) {
        this.x = x;
        this.y = y;
        if (mx - x == 0)
            x--;
        if (my - y == 0)
            this.y++;
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
     * Move the bullet based on the amount of time that has passed.
     * 
     * @param dt time passed since last run of game loop, in milliseconds.
     */
    public void move(long dt) {
        double t = (double)dt;
        double dx;
        if (t < VEL)
            dx = Math.sqrt((VEL*VEL) / (1 + (slope*slope)));
        else
            dx = Math.sqrt((t*t) / (1 + (slope*slope)));
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
     * Draw the bullet on a shifted coordinate system.
     * @param g Graphics context in which to draw the bullet.
     * @param dx x-shift.
     * @param dy y-shift.
     */
    public void drawWithShift(Graphics g, int dx, int dy) {
        if (human)
            g.drawImage(hsprite, x + dx, y + dy, null);
        else
            g.drawImage(csprite, x + dx, y + dy, null);
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
    
    /**
     * Get the bounding box of the bullet.
     * 
     * @return A 10x10 rectangle that contains the bullet.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, 10, 10);
    }
    
    /**
     * Get the bounding box of the bullet in a shifted coordinate system.
     * 
     * @param offx x-shift.
     * @param offy y-shift.
     * @return A 10x10 rectangle that contains the bullet, shifted.
     */
    public Rectangle getShiftedBounds(int offx, int offy) {
        return new Rectangle(x+offx, y+offy, 10, 10);
    }
}
