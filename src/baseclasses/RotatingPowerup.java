/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

/**
 *
 * @author Jacky Tian
 */
public abstract class RotatingPowerup implements Powerup {
    private int x;
    private int y;
    private int direction;
    
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
    
    /**
     * @return the x-coordinate of the powerup.
     */
    @Override
    public int getX() {
        return x;
    }

    /**
     * @return the y-coordinate of the powerup.
     */
    @Override
    public int getY() {
        return y;
    }
}
