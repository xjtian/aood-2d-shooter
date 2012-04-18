/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import java.util.ArrayList;

/**
 *
 * @author tbaile1121
 */
public class HeuristicPathfinder {
    public static void move(int cx, int cy, int x, int y, boolean[][] m) {
        //If |dx| > |dy|, move vertically towards target.
        //If |dx| > |dy|, move horizontally
        //If |dx| = |dy|, move diagonally
        //If the grid square to move to is occupied, execute subroutine that navigates
        //around the obstacle. If computer needs to move down, navigate right/left
        //then down to clear. 
        
        int gx = (cx / 20);
        int gy = (cy / 20);
        int tx = (x / 20);
        int ty = (x / 20);
        
        int dis = (int) Math.sqrt(((gx-tx)*(gx-tx)) + ((gy-ty)*(gy-ty))); //distance between the target and curent player
        boolean[][] map = m;
        ArrayList<MoveType> moves = new ArrayList<MoveType>();
        
        for (int i = 0; i < dis; i++) {
            
                if (cy < y) {//if the current is below the target
                    if (!map[cx][cy+1]) {//as long as the map in that direction does not contain barrier
                        moves.add(MoveType.UP);//move up
                    }
                }else if(cy > y) {//if the current is above the target
                    if (!map[cx][cy-1]) {//as long as the map in that direction does not contain barrier
                        moves.add(MoveType.DOWN);//move down
                    }
                }else if(cx > x) {//if the current is to the right of the target
                    if (!map[cx - 1][cy]) {//as long as the map in that direction does not contain barrier
                        moves.add(MoveType.LEFT);//move left
                    }
                }else if(cx < x) {//if the current is to the left of the target
                    if (!map[cx+1][cy]) {//as long as the map in that direction does not contain barrier
                        moves.add(MoveType.RIGHT);//move right
                    }
                }
                //adjusts the position of the current
                if (moves.remove(0) == MoveType.DOWN)
                    cy--;
                if (moves.remove(0) == MoveType.UP)
                    cy++;
                if (moves.remove(0) == MoveType.RIGHT)
                    cx--;
                if (moves.remove(0) == MoveType.LEFT)
                    cx++;
                //updates the distance(in theory)
                dis = (int) Math.sqrt(((gx-tx)*(gx-tx)) + ((gy-ty)*(gy-ty)));
           // }
        }
        
        
       
        
        
    }
    
    private enum MoveType {
        UP, LEFT, RIGHT, DOWN;
    }
}
