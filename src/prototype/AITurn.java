/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import baseclasses.BarrierFactory;
import baseclasses.Bullet;
import baseclasses.ComputerSpriteLoader;
import baseclasses.PlayerSpriteLoader;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author xtian8741
 */
public class AITurn extends Applet implements Runnable {

    private boolean[] commands = new boolean[32767];    //key and mouse commands
    private int x = 200;    //player x
    private int y = 200;    //player y
    private int health = 100;
    
    private int cx = 300;   //cpu x
    private int cy = 300;   //cpu y
    private int chealth = 100;
    
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
        long lastTime = System.currentTimeMillis();
        long loopController = System.currentTimeMillis();
        gameloop: while (stopFlag) {
//            if (System.currentTimeMillis() - loopController < 4)    //upper limit 250fps
//                continue gameloop;
//            else
//                loopController = System.currentTimeMillis();
            long t = System.currentTimeMillis() - loopController;
            if (t < 4) {
                try {
                    Thread.sleep(4 - t);
                } catch (InterruptedException e) {}
            }
            loopController = t;
            
            long interval = System.currentTimeMillis() - lastTime;
            if (interval > 100) 
                lastTime = System.currentTimeMillis();
            
            double radians = Math.atan2((double) (y + 20 - my), (double) (x + 20 -mx));
            int dir = (int)Math.toDegrees(radians);
            
            dir = (dir%15 > 7) ? dir+(15 - dir%15):dir - dir%15;
            dir -=90;
            
            if (dir < 0)
                dir = 360 + dir;
            if (dir == 360)
                dir = 0;
            
            double cradians = Math.atan2((double) (cy - y - 15), (double) (cx - x - 15));
            int cdir = (int)Math.toDegrees(cradians);
            
            cdir = (cdir%15 > 7) ? cdir+(15 - cdir%15):cdir - cdir%15;
            cdir -=90;
            
            if (cdir < 0)
                cdir = 360 + cdir;
            if (cdir == 360)
                cdir = 0;
            
            if (commands[KeyEvent.VK_A]) {
                x--;
                if (x < 0)
                    x++;
                for (Polygon p : barriers) {
                    if (p.intersects(new Rectangle(x, y, 40, 40))) {
                        x++;
                        break;
                    }
                }
            }
            if (commands[KeyEvent.VK_D]) {
                x++;
                if (x > 360)
                    x--;
                for (Polygon p : barriers) {
                    if (p.intersects(new Rectangle(x, y, 40, 40))) {
                        x--;
                        break;
                    }
                }
            }
            if (commands[KeyEvent.VK_W]) {
                y--;
                if (y < 0)
                    y++;
                for (Polygon p : barriers) {
                    if (p.intersects(new Rectangle(x, y, 40, 40))) {
                        y++;
                        break;
                    }
                }
            }
            if (commands[KeyEvent.VK_S]) {
                y++;
                if (y > 360)
                    y--;
                for (Polygon p : barriers) {
                    if (p.intersects(new Rectangle(x, y, 40, 40))) {
                        y--;
                        break;
                    }
                }
            }
            
            if (interval > 100) {
                if (commands[MouseEvent.BUTTON1])
                    userbullets.add(new Bullet(x+15, y+15, mx, my, true));
                if (Math.random() < .5)
                    cpubullets.add(new Bullet(cx+5, cy+5, x, y, false));
            }
            
            if (interval > 10) {
                for (Bullet b : userbullets)
                    b.move();
                for (Bullet b : cpubullets)
                    b.move();
            }
            
            inuserbullets: for (int i = userbullets.size() -1; i >= 0; i--) {
                for (int j = 0; j < barriers.length; j++) {
                    if (barriers[j].intersects(userbullets.get(i).getBounds())) {
                        userbullets.remove(i);
                        continue inuserbullets;
                    }
                }
                
                int bx = userbullets.get(i).getX();
                int by = userbullets.get(i).getY();
                if (bx < 0 || bx > 400 || by < 0 || by > 400) {
                    userbullets.remove(i);
                    continue inuserbullets;
                }
                
                if (userbullets.get(i).getBounds().intersects(new Rectangle(cx, cy, 20, 20))) {
                    userbullets.remove(i);
                    chealth-=2;
                }
            }
            
            incpubullets: for (int i = cpubullets.size() -1; i >= 0; i--) {
                for (int j = 0; j < barriers.length; j++) {
                    if (barriers[j].intersects(cpubullets.get(i).getBounds())) {
                        cpubullets.remove(i);
                        continue incpubullets;
                    }
                }
                
                int bx = cpubullets.get(i).getX();
                int by = cpubullets.get(i).getY();
                if (bx < 0 || bx > 400 || by < 0 || by > 400) {
                    cpubullets.remove(i);
                    continue incpubullets;
                }
                
                if (cpubullets.get(i).getBounds().intersects(new Rectangle(x, y, 40, 40))) {
                    cpubullets.remove(i);
                    health-=2;
                }
            }
            
            //Paint all components to buffer
            ogr = buffer.createGraphics();
            ogr.setColor(Color.white);
            ogr.fillRect(0, 0, 400, 400);
            ogr.drawImage(bgimage, null, 0, 0);
            ogr.drawImage(PlayerSpriteLoader.getSprite(dir), x, y, null);
            ogr.drawImage(ComputerSpriteLoader.getSprite(cdir), cx, cy, null);
            ogr.setColor(Color.green);
            ogr.fillRect(380, 10, 10, health / 2);
            ogr.setColor(Color.red);
            ogr.fillRect(360, 10, 10, chealth / 2);
            for (Bullet b : userbullets)
                b.draw(ogr);
            for (Bullet b : cpubullets)
                b.draw(ogr);
            ogr.dispose();
            
            //transfer buffer
            this.getGraphics().drawImage(buffer, 0, 0, null);
            frames++;
            
            if (health <= 0) {
                System.out.println("You lost");
                long time2 = System.currentTimeMillis();
                stopFlag = false;
                int framerate =  (int) (frames / ((time2 - time) / 1000));
                System.out.println("Average " + framerate + "fps");
                System.exit(0);
            }
            
            if (chealth <= 0) {
                System.out.println("You won");
                long time2 = System.currentTimeMillis();
                stopFlag = false;
                int framerate =  (int) (frames / ((time2 - time) / 1000));
                System.out.println("Average " + framerate + "fps");
                System.exit(0);
            }
        }
    }
    
    @Override
    public void stop() {
        long time2 = System.currentTimeMillis();
        stopFlag = false;
        try {
            gameLoop.join();
        } catch (InterruptedException ex) { }
        gameLoop = null;
        
        int framerate =  (int) (frames / ((time2 - time) / 1000));
        System.out.println("Average " + framerate + "fps");
    }
    
    @Override
    public void processEvent(AWTEvent e) {
        boolean down = false;
        switch(e.getID()) {
            case KeyEvent.KEY_PRESSED:
                down = true;
            case KeyEvent.KEY_RELEASED:
                commands[((KeyEvent) e).getKeyCode()] = down;
                break;
            case MouseEvent.MOUSE_PRESSED:
                down = true;
            case MouseEvent.MOUSE_RELEASED:
                commands[((MouseEvent) e).getButton()] = down;
            case MouseEvent.MOUSE_MOVED:
            case MouseEvent.MOUSE_DRAGGED:
                mx = ((MouseEvent) e).getX();
                my = ((MouseEvent) e).getY();
                break;
        }
    }
    
}
