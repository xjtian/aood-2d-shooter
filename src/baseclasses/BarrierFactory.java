/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

import java.awt.Polygon;

/**
 *
 * @author jacky
 */
public class BarrierFactory {
    private static int gridSize = 20;
    
    public static void setGridSize(int size) {
        gridSize = size;
    }
    
    public static Polygon generateRectangleBlock(int gx, int gy, int gw, int gh) {
        return new Polygon(new int[]{gx * gridSize, gx * gridSize, (gx+gw) * gridSize, (gx+gw)*gridSize}, 
                new int[]{gy * gridSize, (gy+gh)*gridSize, (gy+gh)*gridSize, gy*gridSize}, 4);
    }
    
    
    public static Polygon generateLeftLBlock(int gx, int gy, int gw, int gh) {
        return new Polygon(new int[]{(gx+gw-1)*gridSize, (gx+gw)*gridSize, (gx+gw)*gridSize, 
            gx*gridSize, gx*gridSize, (gx+gw-1)*gridSize}, 
                new int[]{gy*gridSize, gy*gridSize, (gy+gh)*gridSize, 
                    (gy+gh)*gridSize, (gy+gh-1)*gridSize, (gy+gh-1)*gridSize}, 6);
    }
    
    public static Polygon generateRightLBlock(int gx, int gy, int gw, int gh) {
        return new Polygon(new int[]{gx*gridSize, (gx+1)*gridSize, (gx+1)*gridSize, 
            (gx+gw)*gridSize, (gx+gw)*gridSize, gx*gridSize}, 
                new int[]{gy*gridSize, gy*gridSize, (gy+gh-1)*gridSize, 
                    (gy+gh-1)*gridSize, (gy+gh)*gridSize, (gy+gh)*gridSize}, 6);
    }
}
