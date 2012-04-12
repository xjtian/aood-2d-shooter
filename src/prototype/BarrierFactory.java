/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

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
    
    public static Polygon generateRectangleBlock(int x, int y, int gw, int gh) {
        return new Polygon(new int[]{x, x, x+(gw * gridSize), x+(gw * gridSize)}, 
                new int[]{y, y+(gh*gridSize), y+(gh*gridSize), y}, 4);
    }
    
    
    public static Polygon generateLeftLBlock(int x, int y, int gw, int gh) {
        throw new UnsupportedOperationException();
    }
    
    public static Polygon generateRightLBlock(int x, int y, int gw, int gh) {
        throw new UnsupportedOperationException();
    }
}
