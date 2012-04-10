/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import java.awt.Rectangle;

/**
 *
 * @author xtian8741
 */
public class BarrierGenerator {
    
    /**
     * Generates a square array representing a map with a variety of random rectangles 
     * spread across the map as barriers.
     * 
     * @param polygons How many rectangles to generate.
     * @param dimension The side-length, in pixels, of the image.
     * @return A 2D array with pixel color values.
     */
    public static int[][] generateMap(int polygons, int dimension) {
        int[][] map = new int[dimension][dimension];
        Rectangle[] shapes = new Rectangle[polygons];
        
        int index = 0;
        //Make some random Rectangles.
        while (index < shapes.length) {
            //Code
            
            /*
             * 1. Generate rectangle WxH @ (x,y)
             * 2. Check previously instantiated rectangles, check if this rectangle 
             * intersects any others. If yes, go back to 1.
             * 3. Add this rectangle to the array at index #index
             * 4. index++
             */
        }
        
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                //Set stuff by pixels. Hint: check contains(int x, int y) method defined in Rectangle
                
                /*
                 * 1. N = 0
                 * 2. Check if shapes[N] contains (i,j). If yes, 3. If no, N++, repeat 2 until N = shapes.length.
                 * 3. Set map[i][j] to hexcode for black color.
                 * 4. Terminate, move on.
                 */
            }
        }
        throw new UnsupportedOperationException();
    }
}
