/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import java.awt.Rectangle;
import java.util.Random;
import java.util.*;
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
        double r = Math.random();
        int[] fin = new int[dimension * dimension];
        Random rnd = new Random();
        Rectangle g;
        int index = 0;
        boolean notCon;
        int w, l, x, y;
        int rndtrack;
        //Make some random Rectangles.
        while (index <= shapes.length) {
            //Code
            w = rnd.nextInt(dimension);
            l = rnd.nextInt(dimension);
            x = rnd.nextInt(dimension);
            y = rnd.nextInt(dimension);
            g = new Rectangle(w, l, x, y);
            for(int i = index; i >= 0; i--){
                if(!shapes[i].contains(x, y)){
                    //shapes[index].add(g);
                    notCon = true;
                }else{
                    notCon = false;
                } 
            }
            if(notCon = true){
                shapes[index].add(g);
            }
            index++;
            /*
             * 1. Generate rectangle WxH @ (x,y)
             * 2. Check previously instantiated rectangles, check if this rectangle 
             * intersects any others. If yes, go back to 1.
             * 3. Add this rectangle to the array at index #index
             * 4. index++
             */
        }
        
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < dimension; j++) {//map[i].length
                //Set stuff by pixels. Hint: check contains(int x, int y) method defined in Rectangle
                int n = 0;
                while(n != shapes.length){
                
                if(shapes[n].contains(i, j)){
                    map[i*j] = 0x00000;
                }else{
                    n++;
                }
                }
                /*
                 * 1. N = 0
                 * 2. Check if shapes[N] contains (i,j). If yes, 3. If no, N++, repeat 2 until N = shapes.length.
                 * 3. Set map[i][j] to hexcode for black color.
                 * 4. Terminate, move on.
                 */
            }
        }
        //throw new UnsupportedOperationException();
        return map;
    }
}
