/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

/**
 *
 * @author xtian8741
 */
public class Player {
    protected int direction;
    protected int x;
    protected int y;
    protected int health;
    
    public Player(int x, int y) {
        health = 200;
        this.x = x;
        this.y = y;
        direction = 0;
    }
    
    //@TODO: setter and getter methods in Player for each field
    
    //@TODO: movement methods in Player for each field.
    
    //@TODO: draw method in Player
    
}
