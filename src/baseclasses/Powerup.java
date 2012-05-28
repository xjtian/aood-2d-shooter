/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 * Interface for a generic powerup.
 * 
 * @author Jacky Tian
 */
public interface Powerup {
    
    /**
     * Paint the powerup.
     * 
     * @param g Graphics context in which to paint.
     */
    void draw(Graphics2D g);
    /**
     * Paint the powerup on a shifted coordinate system.
     * @param g Graphics context in which to paint.
     * @param x x-shift.
     * @param y y-shift.
     */
    void drawWithShift(Graphics2D g, int x, int y);
    /**
     * Get the bounding box of the powerup.
     * @return The bounding box of the powerup.
     */
    Rectangle getBounds();
    /**
     * Get the name of the powerup.
     * @return The name of the powerup.
     */
    String getName();
    /**
     * Get the type of the powerup.
     * @return The type of the powerup.
     */
    PowerupType getType();
    
    /**
     * @return the x-coordinate of the powerup.
     */
    int getX();
    /**
     * @return the y-coordinate of the powerup.
     */
    int getY();
    /**
     * Change the powerup state - this implementation is up to the client class.
     * 
     * @param d Change data.
     */
    void mutate(int d);
    
}
