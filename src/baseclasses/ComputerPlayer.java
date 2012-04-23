/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

/**
 *
 * @author xtian8741
 */
public class ComputerPlayer extends Player {
    public ComputerPlayer(int x, int y) {
        super(x, y);
        super.health = 100;
    }
}
