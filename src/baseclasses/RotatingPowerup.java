/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

/**
 * An abstract powerup that is animated by rotating in place.
 * 
 * @author Jacky Tian
 */
public abstract class RotatingPowerup implements Powerup {
    private int x;
    private int y;
    private int direction;
    
    /**
     * Create a new rotating powerup at specified location.
     * @param x x-coordinate.
     * @param y y-coordinate.
     */
    public RotatingPowerup(int x, int y) {
        this.x = x;
        this.y = y;
        direction = 0;
    }
    
    @Override
    public void mutate(int d) {
        direction += 15;
        if (direction >= 360) direction -= 360;
    }

    public int getDirection() {
        return direction;
    }
    
    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
