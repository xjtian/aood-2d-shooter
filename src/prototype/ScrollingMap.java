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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 *
 * @author xtian8741
 */
public class ScrollingMap extends Applet implements Runnable {
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
    private ArrayList<Polygon> barriers; //all barriers in map
    
    private ArrayList<Bullet> userbullets;  //yellow bullets
    private ArrayList<Bullet> cpubullets;   //red bullets
    
    private BufferedImage buffer;   //offscreen buffer
    private Graphics2D ogr; //offscreen graphics handle
    
    private int camx;   //camera x-offset
    private int camy;   //camera y-offset
    
    private static final int DIM = 1200;
    
    {
        PlayerSpriteLoader.loadAllImages();
        ComputerSpriteLoader.loadAllImages();
        bgimage = new BufferedImage(1200, 1200, BufferedImage.BITMASK);
        
        buffer = new BufferedImage(400, 400, BufferedImage.BITMASK);
        
        //instantiate map barriers
        barriers = new ArrayList<Polygon>();
        //0, 0
        barriers.add(BarrierFactory.generateRightFlippedL(5, 4, 3, 5));
        barriers.add(BarrierFactory.generateLeftLBlock(9, 4, 3, 5));
        barriers.add(BarrierFactory.generateRectangleBlock(14, 10, 8, 2));
        barriers.add(BarrierFactory.generateRightLBlock(3, 16, 5, 3));
        //0, 1
        barriers.add(BarrierFactory.generateRightFlippedL(5, 23, 4, 3));
        barriers.add(BarrierFactory.generateRightFlippedL(5, 26, 4, 5));
        barriers.add(BarrierFactory.generateLeftFlippedL(12, 30, 4, 4));
        barriers.add(BarrierFactory.generateLeftFlippedL(12, 34, 4, 3));
        //0, 2
        barriers.add(BarrierFactory.generateRightFlippedL(2, 41, 3, 3));
        barriers.add(BarrierFactory.generateRightFlippedL(2, 44, 3, 3));
        barriers.add(BarrierFactory.generateRightLBlock(7, 46, 2, 4));
        barriers.add(BarrierFactory.generateLeftLBlock(9, 46, 3, 4));
        barriers.add(BarrierFactory.generateRectangleBlock(14, 51, 1, 5));
        barriers.add(BarrierFactory.generateLeftFlippedL(15, 52, 4, 4));
        //1, 0
        barriers.add(BarrierFactory.generateLeftFlippedL(21, 3, 3, 2));
        barriers.add(BarrierFactory.generateLeftLBlock(23, 3, 3, 2));
        barriers.add(BarrierFactory.generateRightFlippedL(35, 12, 3, 5));
        barriers.add(BarrierFactory.generateRectangleBlock(36, 16, 2, 1));
        //1, 1
        barriers.add(BarrierFactory.generateRectangleBlock(22, 22, 6, 1));
        barriers.add(BarrierFactory.generateRectangleBlock(26, 26, 7, 1));
        barriers.add(BarrierFactory.generateRectangleBlock(28, 30, 8, 1));
        barriers.add(BarrierFactory.generateRectangleBlock(31, 34, 8, 1));
        barriers.add(BarrierFactory.generateRightLBlock(22, 33, 5, 6));
        //1, 2
        barriers.add(BarrierFactory.generateRightFlippedL(21, 46, 6, 5));
        barriers.add(BarrierFactory.generateRectangleBlock(22, 50, 17, 1));
        barriers.add(BarrierFactory.generateLeftFlippedL(31, 46, 8, 4));
        barriers.add(BarrierFactory.generateRightLBlock(26, 54, 2, 4));
        barriers.add(BarrierFactory.generateLeftLBlock(31, 54, 2, 4));
        //2, 0
        barriers.add(BarrierFactory.generateRectangleBlock(43, 5, 12, 2));
        barriers.add(BarrierFactory.generateRightLBlock(48, 7, 7, 6));
        barriers.add(BarrierFactory.generateRectangleBlock(43, 12, 5, 1));
        //2, 1
        barriers.add(BarrierFactory.generateRightLBlock(42, 21, 4, 5));
        barriers.add(BarrierFactory.generateLeftFlippedL(46, 22, 4, 7));
        barriers.add(BarrierFactory.generateRectangleBlock(53, 21, 6, 2));
        barriers.add(BarrierFactory.generateRightFlippedL(43, 28, 3, 6));
        barriers.add(BarrierFactory.generateLeftLBlock(47, 28, 6, 6));
        barriers.add(BarrierFactory.generateLeftLBlock(52, 29, 6, 9));
        //2, 2
        barriers.add(BarrierFactory.generateRightLBlock(46, 43, 2, 3));
        barriers.add(BarrierFactory.generateRectangleBlock(54, 48, 2, 2));
        barriers.add(BarrierFactory.generateRightLBlock(42, 54, 8, 3));
        barriers.add(BarrierFactory.generateRectangleBlock(50, 55, 2, 2));
        barriers.add(BarrierFactory.generateRectangleBlock(52, 56, 7, 1));
        barriers.add(BarrierFactory.generateRectangleBlock(53, 54, 1, 2));
        
        map = new boolean[60][60];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                for (Polygon p : barriers) {
                    if (p.contains(20*i + 10, 20*j + 10)) {
                        map[i][j] = true;
                        break;
                    }
                }
            }
        }
        
        //draw the whole map
        Graphics2D bg = bgimage.createGraphics();
        bg.setColor(Color.black);
        for (Polygon p : barriers) {
            bg.fillPolygon(p);
        }
        bg.dispose();
        
        userbullets = new ArrayList<Bullet>();
        cpubullets = new ArrayList<Bullet>();
        
        //Add all players
        cpuPlayers = new ArrayList<ComputerPlayer>();
        cpuPlayers.add(new ComputerPlayer(460, 460));
        cpuPlayers.add(new ComputerPlayer(500, 500));
        cpuPlayers.add(new ComputerPlayer(700, 420));
        
        //@TODO: Evenly distribute cpu players across every 'grid'
        //@TODO: CPU players are stationary until they are first discovered?
        
        human = new Player(600, 640);
        camx = human.getX() - 180;
        camy = human.getY() - 180;
    }
    
    @Override
    public void start() {
        enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
        setSize(400, 400);
        time = System.currentTimeMillis();
        frames = 0;
        stopFlag = true;
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    @Override
    public void run() {
        long loopController = System.currentTimeMillis();
        gameloop: while(stopFlag) {
            long dt = System.currentTimeMillis() - loopController;
            if (dt > 50)
                loopController = System.currentTimeMillis();
            
            //computations regarding human and computer directions
            double radians = Math.atan2((double) (human.getY() + 20 - my - camy), (double) (human.getX() + 20 - mx - camx));
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
            
            if (commands[KeyEvent.VK_A] || commands[KeyEvent.VK_LEFT]) {
                human.move(-umove, 0);
                if (human.getX() < 0)
                    human.move(umove, 0);
                for (Polygon p : barriers) {
                    if (p.intersects(human.getBounds())) {
                        human.move(umove, 0);
                        break;
                    }
                }
            }
            if (commands[KeyEvent.VK_D] || commands[KeyEvent.VK_RIGHT]) {
                human.move(umove, 0);
                if (human.getX() > DIM - 40)
                    human.move(-umove, 0);
                for (Polygon p : barriers) {
                    if (p.intersects(human.getBounds())) {
                        human.move(-umove, 0);
                        break;
                    }
                }
            }
            if (commands[KeyEvent.VK_W] || commands[KeyEvent.VK_UP]) {
                human.move(0, -umove);
                if (human.getY() < 0)
                    human.move(0, umove);
                for (Polygon p : barriers) {
                    if (p.intersects(human.getBounds())) {
                        human.move(0, umove);
                        break;
                    }
                }
            }
            if (commands[KeyEvent.VK_S] || commands[KeyEvent.VK_DOWN]) {
                human.move(0, umove);
                if (human.getY() > DIM - 40)
                    human.move(0, -umove);
                for (Polygon p : barriers) {
                    if (p.intersects(human.getBounds())) {
                        human.move(0, -umove);
                        break;
                    }
                }
            }
            
            //Start cpu movement logic
            int dx, dy;
            boolean inters = false;
            int cmove = (int)(dt / 200) + 1;
            for (ComputerPlayer cp : cpuPlayers) {
                //horizontal movement
                dx = human.getX() - cp.getX();
                if (dx < 0)
                    cp.move(-cmove, 0);
                else
                    cp.move(cmove, 0);
                if (cp.getX() < DIM-20 && cp.getX() > 0) {
                    collisions: for (Polygon p : barriers) {
                        inters = p.intersects(cp.getBounds());
                        if (inters) break collisions;
                    }
                    
                    cpucollide: for (ComputerPlayer cp2 : cpuPlayers) {
                        if (inters) break cpucollide;
                        if (cp2 != cp) {
                            inters = cp.getBounds().intersects(cp2.getBounds());
                            if (inters) break cpucollide;
                        }
                    }
                } else {
                    if (dx < 0)
                        cp.move(cmove, 0);
                    else
                        cp.move(-cmove, 0);
                }
                
                if (inters) {
                    if (dx < 0)
                        cp.move(cmove, 0);
                    else
                        cp.move(-cmove, 0);
                    inters = false;
                }
                //vertical movement
                dy = human.getY() - cp.getY();
                if (dy < 0)
                    cp.move(0, -cmove);
                else
                    cp.move(0, cmove);
                if (cp.getY() < DIM-20 && cp.getY() > 0) {
                    collisions: for (Polygon p : barriers) {
                        inters = p.intersects(cp.getBounds());
                        if (inters) break collisions;
                    }
                    
                    cpucollide: for (ComputerPlayer cp2 : cpuPlayers) {
                        if (inters) break cpucollide;
                        if (cp2 != cp) {
                            inters = cp.getBounds().intersects(cp2.getBounds());
                            if (inters) break cpucollide;
                        }
                    }
                } else {
                    if (dy < 0)
                        cp.move(0, cmove);
                    else
                        cp.move(0, -cmove);
                }
                
                if (inters) {
                    if (dy < 0)
                        cp.move(0, cmove);
                    else
                        cp.move(0, -cmove);
                    inters = false;
                }
            }
            
            //Shooting bullets
            if (dt > 50) {
                if (commands[MouseEvent.BUTTON1])
                    userbullets.add(new Bullet(human.getX()+15-camx, human.getY()+15-camy, mx, my, true));
                for (ComputerPlayer cp : cpuPlayers) {
                    if (Math.random() < .5)
                        cpubullets.add(new Bullet(cp.getX() + 5-camx, cp.getY() + 5-camy, human.getX()-camx, human.getY()-camy, false));
                }
            }
            
            //Moving bullets
            for (Bullet b : userbullets)
                b.move();
            for (Bullet b : cpubullets)
                b.move();
            
            //checking bullet collisions and bounds
            inuserbullets: for (int i = userbullets.size() - 1; i >= 0; i--) {
                for (Polygon p : barriers) {
                    if (p.intersects(userbullets.get(i).getShiftedBounds(camx, camy))) {
                        userbullets.remove(i);
                        continue inuserbullets;
                    }
                }
                
                int bx = userbullets.get(i).getX() + camx;
                int by = userbullets.get(i).getY() + camy;
                if (bx < 0 || bx > DIM || by < 0 || by > DIM) {
                    userbullets.remove(i);
                    continue inuserbullets;
                }
                
                for (ComputerPlayer cp : cpuPlayers) {
                    if (userbullets.get(i).getShiftedBounds(camx, camy).intersects(cp.getBounds())) {
                        userbullets.remove(i);
                        cp.damage(2);
                        continue inuserbullets;
                    }
                }
            }
            
            for (int i = cpuPlayers.size() - 1; i >= 0; i--) {
                if (cpuPlayers.get(i).getHealth() <= 0)
                    cpuPlayers.remove(i);
            }
            
            //checking cpu bullet collisions and bounds
            incpubullets: for (int i = cpubullets.size() -1; i >= 0; i--) {
                for (Polygon p : barriers) {
                    if (p.intersects(cpubullets.get(i).getShiftedBounds(camx, camy))) {
                        cpubullets.remove(i);
                        continue incpubullets;
                    }
                }
                
                int bx = cpubullets.get(i).getX();
                int by = cpubullets.get(i).getY();
                if (bx < 0 || bx > DIM || by < 0 || by > DIM) {
                    cpubullets.remove(i);
                    continue incpubullets;
                }
                
                if (cpubullets.get(i).getShiftedBounds(camx, camy).intersects(human.getBounds())) {
                    cpubullets.remove(i);
                    human.damage(2);
                }
            }
            if (dt > 50) {
                for (ComputerPlayer cp : cpuPlayers) {
                    if (cp.getBounds().intersects(human.getBounds()))
                        human.damage(1);
                }
            }
            
            camx = human.getX() - 180;
            camy = human.getY() - 180;
            
            if (camx <= 0) camx = 0;
            if (camx >= DIM - 400) camx = DIM - 400;
            if (camy <= 0) camy = 0;
            if (camy >= DIM - 400) camy = DIM - 400;
                
            //Paint offscreen
            ogr = buffer.createGraphics();
            ogr.setColor(Color.white);
            ogr.fillRect(0, 0, 400, 400);
            ogr.drawImage(bgimage.getSubimage(camx, camy, 400, 400), 0, 0, null);
            human.draw(ogr, human.getX() - camx, human.getY() - camy);
            for (ComputerPlayer cp : cpuPlayers) {  
                cp.draw(ogr, cp.getX() - camx, cp.getY() - camy);
            }
            int totalbars = 1 + cpuPlayers.size();  //drawing health bars
            ogr.setColor(Color.green);
            ogr.fillRect(400 - 20*totalbars, 10, 10, human.getHealth() / 2);
            ogr.setColor(Color.red);
            for (int i = 0; i < cpuPlayers.size(); i++) {
                ogr.fillRect(400 - 20*(totalbars - i - 1), 10, 10, cpuPlayers.get(i).getHealth() / 2);
            }
            for (Bullet b : userbullets)    //drawing bullets
                b.draw(ogr);
            for (Bullet b : cpubullets)
                b.draw(ogr);
            ogr.dispose();
            
            //flip the buffer
            this.getGraphics().drawImage(buffer, 0, 0, null);
            frames++;
            
            if (human.getHealth() <= 0) {
                System.out.println("You lost");
                long time2 = System.currentTimeMillis();
                stopFlag = false;
                int framerate =  (int) (frames / ((time2 - time) / 1000));
                System.out.println("Average " + framerate + "fps");
                System.exit(0);
            }
            
            if (cpuPlayers.isEmpty()) {
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
        
        disableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
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
