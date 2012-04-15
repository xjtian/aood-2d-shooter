/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import baseclasses.PlayerSpriteLoader;
import java.applet.Applet;
import java.awt.AWTEvent;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * Simple AWT applet that tests how sprite movement will work.
 * 
 * @author Jacky Tian
 */
public class SpriteMoveApplet extends Applet implements Runnable {
    
    private boolean[] commands = new boolean[32767];
    private int x = 200;
    private int y = 200;
    
    private int mx = 200;
    private int my = 100;
    
    private Thread gameLoop;
    private boolean stopFlag = true;
    
    private int frames = 0;
    private long time = System.currentTimeMillis();
    
    BufferedImage bi;
    Graphics ogr;
    
    {
        PlayerSpriteLoader.loadAllImages();
        bi = new BufferedImage(400, 400, BufferedImage.TYPE_INT_RGB);
        ogr = bi.getGraphics();
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
            int face;
            
            if (dir%15 > 7) face = dir + (15 - dir%15);
            else face = dir - dir%15;
            
            face -=90;
            if (face < 0)
                face = 360 + face;
            if (face == 360)
                face = 0;
            
            if (commands[KeyEvent.VK_A]) x--;
            if (commands[KeyEvent.VK_D]) x++;
            if (commands[KeyEvent.VK_W]) y--;
            if (commands[KeyEvent.VK_S]) y++;
            ogr.clearRect(0, 0, 400, 400);
            ogr.drawImage(PlayerSpriteLoader.getSprite(face), x, y, null);
            
            getGraphics().drawImage(bi, 0, 0, null);
            frames++;
            
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {}
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
        
        int framerate = (int) (frames / ((time2 - time) / 1000));
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
