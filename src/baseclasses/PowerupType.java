/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

/**
 * Enumeration of possible powerup types.
 * @author Jacky Tian
 */
public enum PowerupType {
    /**
     * Ammo powerup.
     */
    AMMO(75),
    /**
     * Health powerup.
     */
    HEALTH(150),
    /**
     * Damage upgrade powerup.
     */
    DAMAGE(10);
    
    private final int data;
    PowerupType(int d) {
        this.data = d;
    }
    
    public int getData() {
        return this.data;
    }
}
