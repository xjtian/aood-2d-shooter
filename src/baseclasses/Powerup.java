/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

import java.awt.Graphics2D;
import java.awt.Rectangle;

/**
 *
 * @author Jacky Tian
 */
public interface Powerup {

    void draw(Graphics2D g);

    void drawWithShift(Graphics2D g, int x, int y);

    Rectangle getBounds();

    String getName();

    PowerupType getType();
    
    /**
     * @return the x-coordinate of the powerup.
     */
    int getX();

    /**
     * @return the y-coordinate of the powerup.
     */
    int getY();

    void mutate(int d);
    
}
