/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import baseclasses.BarrierFactory;
import baseclasses.PlayerSpriteLoader;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Applet demonstrating the use of barriers in a map.
 * 
 * @author jacky
 */
public class SpriteMoveBarriers extends Applet implements Runnable {

    private boolean[] commands = new boolean[32767];
    private int x = 200;
    private int y = 200;
    
    private int mx = 200;
    private int my = 100;
    
    private Thread gameLoop;
    private boolean stopFlag = true;
    
    private int frames = 0;
    private long time = System.currentTimeMillis();
    
    private BufferedImage bgimage;
    private boolean[][] map;
    private Polygon[] barriers;
    
    private BufferedImage buffer;
    private Graphics2D ogr;
    
    {
        PlayerSpriteLoader.loadAllImages();
        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        bgimage = gc.createCompatibleImage(400, 400, Transparency.BITMASK);
        
        buffer = gc.createCompatibleImage(400, 400, Transparency.BITMASK);
        
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
    }
    
    @Override
    public void start() {
        enableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        setSize(400, 400);
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    @Override
    public void run() {
        while (stopFlag) {
            double radians = Math.atan2((double) (y + 20 - my), (double) (x + 20 -mx));
            int dir = (int)Math.toDegrees(radians);
            
            dir = (dir%15 > 7) ? dir+(15 - dir%15):dir - dir%15;
            dir -=90;
            
            if (dir < 0)
                dir = 360 + dir;
            if (dir == 360)
                dir = 0;
            
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
            
            ogr = buffer.createGraphics();
            ogr.setColor(Color.white);
            ogr.fillRect(0, 0, 400, 400);
            ogr.drawImage(bgimage, null, 0, 0);
            ogr.drawImage(PlayerSpriteLoader.getSprite(dir), x, y, null);
            ogr.dispose();
            
            this.getGraphics().drawImage(buffer, 0, 0, null);
            frames++;
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
            case MouseEvent.MOUSE_MOVED:
            case MouseEvent.MOUSE_DRAGGED:
                mx = ((MouseEvent) e).getX();
                my = ((MouseEvent) e).getY();
                break;
        }
    }
    
}
