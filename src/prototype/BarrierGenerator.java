/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import java.awt.Rectangle;
import java.util.Random;
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
    public static int[] generateMap(int polygons, int dimension) {
        int[] map = new int[dimension*dimension];
        Rectangle[] shapes = new Rectangle[polygons];
        Random rnd = new Random();
        int index = 0;
        //Make some random Rectangles.
        while (index < shapes.length) {
            //Code
            int w = rnd.nextInt(dimension / 5);
            int l = rnd.nextInt(dimension / 5);
            int x = rnd.nextInt(dimension);
            int y = rnd.nextInt(dimension);
            Rectangle g = new Rectangle(w, l, x, y);
            boolean f = false;
            for(int i = index - 1; i >= 0; i--){
                if (shapes[i].intersects(g)) {
                    f = true;
                    break;
                }
            }
            
            if (!f) {
                shapes[index] = g;
                index++;
            }
        }
        
        int k = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                for (int n = 0; n < shapes.length; n++) {
                    if (shapes[n].contains(i, j)) {
                        map[k] = 0xFF0000;
                        break;
                    }
                }
                k++;
            }
        }
        return map;
    }
}
