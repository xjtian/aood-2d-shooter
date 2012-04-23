/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import baseclasses.*;
import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author xtian8741
 */
public class MultipleAI extends Applet implements Runnable {
    
    private boolean[] commands = new boolean[32767];    //key and mouse commands
    
    private Player human;
    private ArrayList<ComputerPlayer> cpuPlayers;
    
    private int mx = 200;   //mouse x
    private int my = 100;   //mouse y
    
    private Thread gameLoop;    //game loop thread
    private boolean stopFlag = true;    //game loop stop flag
    
    //for framerate calculation
    private int frames = 0;
    private long time = System.currentTimeMillis();
    
    private BufferedImage bgimage;
    private boolean[][] map;    //tile layout of map
    private Polygon[] barriers; //all barriers in map
    
    private ArrayList<Bullet> userbullets;  //yellow bullets
    private ArrayList<Bullet> cpubullets;   //red bullets
    
    private BufferedImage buffer;   //offscreen buffer
    private Graphics2D ogr; //offscreen graphics handle
    
    {
        PlayerSpriteLoader.loadAllImages();
        ComputerSpriteLoader.loadAllImages();
        bgimage = new BufferedImage(400, 400, BufferedImage.BITMASK);
        
        buffer = new BufferedImage(400, 400, BufferedImage.BITMASK);
        
        barriers = new Polygon[4];
        barriers[0] = BarrierFactory.generateLeftLBlock(8, 10, 6, 3);
        barriers[1] = BarrierFactory.generateRectangleBlock(2, 2, 3, 1);
        barriers[2] = BarrierFactory.generateRightLBlock(8, 0, 2, 4);
        barriers[3] = BarrierFactory.generateLeftFlippedL(4, 13, 2, 4);
        
        map = new boolean[20][20];
        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                for (Polygon p : barriers) {
                    if (p.contains(20*i + 10, 20*j + 10)) {
                        map[i][j] = true;
                        break;
                    }
                }
            }
        }
        
        Graphics2D bg = bgimage.createGraphics();
        bg.setColor(Color.black);
        for (Polygon p : barriers) {
            bg.fillPolygon(p);
        }
        bg.dispose();
        
        userbullets = new ArrayList<Bullet>();
        cpubullets = new ArrayList<Bullet>();
        
        cpuPlayers.add(new ComputerPlayer(60, 60));
        cpuPlayers.add(new ComputerPlayer(300, 300));
    }
    
    @Override
    public void start() {
        enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
        setSize(400, 400);
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    @Override
    public void run() {
        long loopController = System.currentTimeMillis();
        gameloop: while(stopFlag) {
            long dt = System.currentTimeMillis() - loopController;
            if (dt > 100)
                loopController = System.currentTimeMillis();
            
            double radians = Math.atan2((double) (human.getY() + 20 - my), (double) (human.getX() + 20 - mx));
            int dir = (int)Math.toDegrees(radians);
            
            dir = (dir%15 > 7) ? dir+(15 - dir%15):dir - dir%15;
            dir -=90;
            
            if (dir < 0)
                dir = 360 + dir;
            if (dir == 360)
                dir = 0;
            
            human.setDirection(dir);
            
            double cradians;
            int cdir;
            for (ComputerPlayer cp : cpuPlayers) {
                cradians = Math.atan2((double) (cp.getY() - human.getY() - 15), (double) (cp.getX() - human.getX() - 15));
                cdir = (int)Math.toDegrees(cradians);

                cdir = (cdir%15 > 7) ? cdir+(15 - cdir%15):cdir - cdir%15;
                cdir -=90;

                if (cdir < 0)
                    cdir = 360 + cdir;
                if (cdir == 360)
                    cdir = 0;
                
                cp.setDirection(cdir);
            }
            
            //User movement max 3 px every 100ms
            int umove = (int) (dt / 100) + 2;
            
            //@TODO: user movement logic using Player class
        }
    }
    
}
