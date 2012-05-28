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
    /**
     * Angular direction from 0-359 degrees.
     */
    protected int direction;
    /**
     * X-coordinate of player.
     */
    protected int x;
    /**
     * Y-coordinate of player.
     */
    protected int y;
    /**
     * Health of player.
     */
    protected int health;
    
    /**
     * Creates a new Player.
     */
    public Player() {
        health = 300;
        direction = 0;
    }
    
    /**
     * Creates a new Player at a specified grid point.
     * 
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public Player(int x, int y) {
        health = 300;
        this.x = x;
        this.y = y;
        direction = 0;
    }
    
    /**
     * Move the player.
     * 
     * @param dx x movement
     * @param dy y movement
     */
    public void move(int dx, int dy) {
        x += dx;
        y += dy;
    }
    
    /**
     * Set the player's position (teleport).
     * 
     * @param x x-coordinate to move to.
     * @param y y-coordinate to move to.
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Shoot a bullet.
     * 
     * @param mx x-coordinate of the point to shoot at.
     * @param my y-coordinate of the point to shoot at.
     * @return A <code>Bullet</code> object that will move along the specified path.
     */
    public Bullet shoot(int mx, int my) {
        return new Bullet(x+15, y+15, mx, my, true);
    }
    
    /**
     * Get x-coordinate of upper-left corner.
     * 
     * @return x-coordinate of upper-left corner.
     */
    public int getX() {
        return x;
    }
    
    /**
     * Get y-coordinate of upper-left corner.
     * 
     * @return y-coordinate of upper-left corner.
     */
    public int getY() {
        return y;
    }
    
    /**
     * Deal damage to player.
     * @param damage Amount of damage to deal.
     */
    public void damage(int damage) {
        health -= damage;
    }
    
    /**
     * Heal the player.
     * @param heal Amount of health to heal.
     */
    public void heal(int heal) {
        health += heal;
    }
    
    /**
     * Set the direction of the player.
     * 
     * @param dir Angular direction from 0 - 359 degrees. 0 is north.
     */
    public void setDirection(int dir) {
        this.direction = dir;
    }
    
    /**
     * Get the direction of the player.
     * 
     * @return The angular direction of the player.
     */
    public int getDirection() {
        return direction;
    }
    
    /**
     * Get the health of the player.
     * 
     * @return The health of the player.
     */
    public int getHealth() {
        return health;
    }
    
    /**
     * Get the bounding box that contains the player.
     * 
     * @return The bounding box that contains the player.
     */
    public java.awt.Rectangle getBounds() {
        return new java.awt.Rectangle(x+2, y+2, 36, 36);
    }
    
    /**
     * Draw the player.
     * @param g The graphics context in which to draw.
     */
    public void draw(Graphics2D g) {
        g.drawImage(PlayerSpriteLoader.getSprite(direction), x, y, null);
    }
    
    /**
     * Draw the player with a direction.
     * @param g The graphics context in which to draw.
     * @param dir What direction the player should be facing.
     */
    public void draw(Graphics2D g, int dir) {
        this.direction = dir;
        g.drawImage(PlayerSpriteLoader.getSprite(direction), x, y, null);
    }
    
    /**
     * Draw the player.
     * @param g The graphics context in which to draw.
     * @param ix x-coordinate of where to draw the player.
     * @param iy y-coordinate of where to draw the player.
     */
    public void draw(Graphics2D g, int ix, int iy) {
        g.drawImage(PlayerSpriteLoader.getSprite(direction), ix, iy, null);
    }
    
    /**
     * Draw the player.
     * @param g The graphics context in which to draw.
     * @param dir What direction the player should be facing.
     * @param ix x-coordinate of where to draw the player.
     * @param iy y-coordinate of where to draw the player.
     */
    public void draw(Graphics2D g, int dir, int ix, int iy) {
        this.direction = dir;
        g.drawImage(PlayerSpriteLoader.getSprite(direction), ix, iy, null);
    }
    
}
