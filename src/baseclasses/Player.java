/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

import java.awt.Graphics2D;

/**
 * Player class that represents a human player in the game. 
 * 
 * @author Jacky Tian
 */
public class Player {
    protected int direction;
    protected int x;
    protected int y;
    protected int health;
    
    public Player(int x, int y) {
        health = 300;
        this.x = x;
        this.y = y;
        direction = 0;
    }
    
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void damage(int damage) {
        health -= damage;
    }
    
    public void heal(int heal) {
        health += heal;
    }
    
    public void setDirection(int dir) {
        this.direction = dir;
    }
    
    public int getDirection() {
        return direction;
    }
    
    public int getHealth() {
        return health;
    }
    
    public java.awt.Rectangle getBounds() {
        return new java.awt.Rectangle(x, y, 40, 40);
    }
    
    public void draw(Graphics2D g) {
        g.drawImage(PlayerSpriteLoader.getSprite(direction), x, y, null);
    }
    
    public void draw(Graphics2D g, int dir) {
        this.direction = dir;
        g.drawImage(PlayerSpriteLoader.getSprite(direction), x, y, null);
    }
    
    public void draw(Graphics2D g, int ix, int iy) {
        g.drawImage(PlayerSpriteLoader.getSprite(direction), ix, iy, null);
    }
    
    public void draw(Graphics2D g, int dir, int ix, int iy) {
        this.direction = dir;
        g.drawImage(PlayerSpriteLoader.getSprite(direction), ix, iy, null);
    }
    
}
