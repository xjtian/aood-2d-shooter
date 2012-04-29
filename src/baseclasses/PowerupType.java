/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

/**
 *
 * @author Jacky Tian
 */
public enum PowerupType {
    AMMO(75),
    HEALTH(150),
    DAMAGE(10);
    
    private final int data;
    PowerupType(int d) {
        this.data = d;
    }
    
    public int getData() {
        return this.data;
    }
}
