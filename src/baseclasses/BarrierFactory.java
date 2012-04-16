/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

import java.awt.Polygon;

/**
 * A factory class that constructs barriers to be placed into a tile map.
 * 
 * @author Jacky Tian
 */
public class BarrierFactory {
    /**
     * Size of each grid square on the tile map.
     */
    private static int gridSize = 20;
    
    /**
     * Set the size of a grid square.
     * 
     * @param size The size, in pixels, of a grid square.
     */
    public static void setGridSize(int size) {
        gridSize = size;
    }
    
    /**
     * Construct a rectangular Polygon.
     * 
     * @param gx x-coordinate of upper-left corner in grid.
     * @param gy y-coordinate of upper-left corner in grid.
     * @param gw width of rectangle in grid.
     * @param gh height of rectangle in grid.
     * @return A <code>Polygon</code> object that is the specified rectangle.
     */
    public static Polygon generateRectangleBlock(int gx, int gy, int gw, int gh) {
        return new Polygon(new int[]{gx * gridSize, gx * gridSize, (gx+gw) * gridSize, (gx+gw)*gridSize}, 
                new int[]{gy * gridSize, (gy+gh)*gridSize, (gy+gh)*gridSize, gy*gridSize}, 4);
    }
    
    /**
     * Construct a left-facing L block (_|). Both legs are 1 grid-square thick.
     * 
     * @param gx x-coordinate of upper-left corner of bounding rectangle.
     * @param gy y-coordinate of upper-left corner of bounding rectangle. 
     * @param gw width of rectangle in grid.
     * @param gh height of rectangle in grid.
     * @return A <code>Polygon</code> object that is the specified L-block.
     */
    public static Polygon generateLeftLBlock(int gx, int gy, int gw, int gh) {
        return new Polygon(new int[]{(gx+gw-1)*gridSize, (gx+gw)*gridSize, (gx+gw)*gridSize, 
            gx*gridSize, gx*gridSize, (gx+gw-1)*gridSize}, 
                new int[]{gy*gridSize, gy*gridSize, (gy+gh)*gridSize, 
                    (gy+gh)*gridSize, (gy+gh-1)*gridSize, (gy+gh-1)*gridSize}, 6);
    }
    
    /**
     * Construct a right-facing L block (L). Both legs are 1 grid-square thick.
     * 
     * @param gx x-coordinate of upper-left corner of bounding rectangle.
     * @param gy y-coordinate of upper-left corner of bounding rectangle. 
     * @param gw width of rectangle in grid.
     * @param gh height of rectangle in grid.
     * @return A <code>Polygon</code> object that is the specified L-block.
     */
    public static Polygon generateRightLBlock(int gx, int gy, int gw, int gh) {
        return new Polygon(new int[]{gx*gridSize, (gx+1)*gridSize, (gx+1)*gridSize, 
            (gx+gw)*gridSize, (gx+gw)*gridSize, gx*gridSize}, 
                new int[]{gy*gridSize, gy*gridSize, (gy+gh-1)*gridSize, 
                    (gy+gh-1)*gridSize, (gy+gh)*gridSize, (gy+gh)*gridSize}, 6);
    }
    
    /**
     * Construct a right-facing upside-down L (|-). Both legs are 1 grid-square thick.
     * 
     * @param gx x-coordinate of upper-left corner of bounding rectangle.
     * @param gy y-coordinate of upper-left corner of bounding rectangle. 
     * @param gw width of rectangle in grid.
     * @param gh height of rectangle in grid.
     * @return A <code>Polygon</code> object that is the specified L-block.
     */
    public static Polygon generateRightFlippedL(int gx, int gy, int gw, int gh) {
        return new Polygon(new int[]{gx*gridSize, (gx+gw)*gridSize, (gx+gw)*gridSize, 
            (gx+1)*gridSize, (gx+1)*gridSize, gx*gridSize}, 
                new int[]{gy*gridSize, gy*gridSize, (gy+1)*gridSize, (
                gy+1)*gridSize, (gy+gh)*gridSize, (gy+gh)*gridSize}, 6);
    }
    
    /**
     * Construct a left-facing upside-down L (-|). Both legs are 1 grid-square thick.
     * 
     * @param gx x-coordinate of upper-left corner of bounding rectangle.
     * @param gy y-coordinate of upper-left corner of bounding rectangle. 
     * @param gw width of rectangle in grid.
     * @param gh height of rectangle in grid.
     * @return A <code>Polygon</code> object that is the specified L-block.
     */
    public static Polygon generateLeftFlippedL(int gx, int gy, int gw, int gh) {
        return new Polygon(new int[]{gx*gridSize, (gx+gw)*gridSize, (gx+gw)*gridSize, 
            (gx+gw-1)*gridSize, (gx+gw-1)*gridSize, gx*gridSize}, 
                new int[]{gy*gridSize, gy*gridSize, (gy+gh)*gridSize, 
                    (gy+gh)*gridSize, (gy+1)*gridSize, (gy+1)*gridSize}, 6);
    }
}
