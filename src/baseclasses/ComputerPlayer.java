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
    public Bullet shoot(int mx, int my) {
        return new Bullet(x+5, y+5, mx, my, false);
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
    
    @Override
    public void draw(Graphics2D g, int ix, int iy) {
        g.drawImage(ComputerSpriteLoader.getSprite(direction), ix, iy, null);
    }
    
    @Override
    public void draw(Graphics2D g, int dir, int ix, int iy) {
        this.direction = dir;
        g.drawImage(ComputerSpriteLoader.getSprite(direction), ix, iy, null);
    }
}
