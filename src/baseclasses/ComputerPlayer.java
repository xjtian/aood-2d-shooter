/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

import java.awt.Graphics2D;

/**
 * Class that represents a computer player in the game.
 * 
 * @author Jacky Tian
 */
public class ComputerPlayer extends Player {
    public ComputerPlayer(int x, int y) {
        super(x, y);
        super.health = 100;
    }
    
    @Override
    public java.awt.Rectangle getBounds() {
        return new java.awt.Rectangle(super.x, super.y, 20, 20);
    }
    
    @Override
    public void draw(Graphics2D g) {
        g.drawImage(ComputerSpriteLoader.getSprite(super.direction), super.x, super.y, null);
    }
    
    @Override
    public void draw(Graphics2D g, int dir) {
        super.direction = dir;
        g.drawImage(ComputerSpriteLoader.getSprite(super.direction), super.x, super.y, null);
    }
}
