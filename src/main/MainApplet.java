/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import baseclasses.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Main applet.
 * 
 * @author Jacky Tian
 */
public class MainApplet extends Applet implements Runnable {
    private boolean[] commands = new boolean[32767];    //key and mouse commands
    
    private Player human;
    private ArrayList<ComputerPlayer> cpuPlayers;
    private ArrayList<ComputerPlayer> frozenCPU;    //computer players that haven't been discovered
    
    private int mx = 200;   //mouse x
    private int my = 100;   //mouse y
    
    private int ammo = CLIP_SIZE;
    private int clips = 5;
    private int bulletdamage = 10;
    
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
    
    private ArrayList<Powerup> powerups;    //powerups
    
    private BufferedImage buffer;   //offscreen buffer
    private BufferedImage pbuffer;
    private Image minimap;  //minimap
    private Graphics2D ogr; //offscreen graphics handle
    
    private int camx;   //camera x-offset
    private int camy;   //camera y-offset
    
    private boolean paused = false;
    private int score = 0;
    
    private int level = 1;
    
    private static final int DIM = 1200;
    private static final int GRID_SIZE = DIM / 20;
    private static final int CLIP_SIZE = 75;
    
    @Override
    public void init() {
        PlayerSpriteLoader.loadAllImages();
        ComputerSpriteLoader.loadAllImages();
        bgimage = new BufferedImage(1200, 1200, BufferedImage.BITMASK);
        
        buffer = new BufferedImage(400, 400, BufferedImage.BITMASK);
        pbuffer = new BufferedImage(400, 400, BufferedImage.BITMASK);
        
        barriers = new ArrayList<Polygon>();
        cpuPlayers = new ArrayList<ComputerPlayer>();
        frozenCPU = new ArrayList<ComputerPlayer>();
        userbullets = new ArrayList<Bullet>();
        cpubullets = new ArrayList<Bullet>();
        powerups = new ArrayList<Powerup>();
        
        human = new Player();
        generateGame(true);
    }
    
    private void generateGame(boolean damage) {
        //instantiate map barriers
        barriers.clear();
        int bplace = 0;
        Polygon temp;
        int choice, x, y, w, h;
        placebarriers: while (bplace < 70) {
            x = (int)(GRID_SIZE*Math.random());
            y = (int)(GRID_SIZE*Math.random());
            w = (int)(9*Math.random())+1;
            h = (int)(9*Math.random())+1;
            choice = (int)(5*Math.random());
            switch (choice) {
                case 0: temp = BarrierFactory.generateLeftFlippedL(x, y, w, h); break;
                case 1: temp = BarrierFactory.generateLeftLBlock(x, y, w, h); break;
                case 2: temp = BarrierFactory.generateRectangleBlock(x, y, w, h); break;
                case 3: temp = BarrierFactory.generateRightFlippedL(x, y, w, h); break;
                case 4: temp = BarrierFactory.generateRightLBlock(x, y, w, h); break;
                default: temp = BarrierFactory.generateRectangleBlock(x, y, w, h);
            }
            
            Rectangle boundsbig = temp.getBounds();
            boundsbig.grow(2, 2);
            Rectangle intboundsbig;
            for (Polygon p : barriers) {
                intboundsbig = p.getBounds();
                intboundsbig.grow(2, 2);
                if (boundsbig.intersects(intboundsbig))
                    continue placebarriers;
            }
            
            barriers.add(temp);
            bplace++;
        }

        map = new boolean[GRID_SIZE][GRID_SIZE];
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
        bg.setColor(Color.white);
        bg.fillRect(0, 0, DIM, DIM);
        bg.setColor(Color.black);
        for (Polygon p : barriers) {
            bg.fillPolygon(p);
        }
        bg.dispose();
        
        minimap = bgimage.getScaledInstance(75, 75, Image.SCALE_SMOOTH);
        
        userbullets.clear();
        cpubullets.clear();
        
        frozenCPU.clear();
        cpuPlayers.clear();
        
        int placecounter = 0;
        int placex, placey;
        placecpu: while (placecounter < 10+3*level) {
            placex = (int) (Math.random() * GRID_SIZE);
            placey = (int) (Math.random() * GRID_SIZE);
            
            if (map[placex][placey])
                continue placecpu;
            
            frozenCPU.add(new ComputerPlayer(placex*20, placey*20));
            placecounter++;
        }
        
        //Generate powerups
        powerups.clear();
        placecounter = 0;
        placepowerups: while (placecounter < 2*level) {
            placex = (int) (Math.random() * GRID_SIZE);
            placey = (int) (Math.random() * GRID_SIZE);
            
            if (map[placex][placey])
                continue placepowerups;
            
            choice = (int)(2*Math.random());
            if (choice == 0)
                powerups.add(new ReloadPowerup(placex*20, placey*20));
            else
                powerups.add(new HealthPowerup(placex*20, placey*20));
            placecounter++;
        }
        
        placedamagepowerup: while(damage) {
            placex = (int) (Math.random() * GRID_SIZE);
            placey = (int) (Math.random() * GRID_SIZE);
            
            if (map[placex][placey])
                continue placedamagepowerup;
            
            powerups.add(new DamagePowerup(placex*20, placey*20));
            break;
        }
        
        Rectangle hr = new Rectangle(((int)(60*Math.random()))*20, ((int)(60*Math.random())*20), 40, 40);
        starthuman: while(true) {
            hr = new Rectangle(((int)(60*Math.random()))*20, ((int)(60*Math.random())*20), 40, 40);
            for (Polygon p : barriers) {
                if (p.intersects(hr))
                    continue starthuman;
            }
            break;
        }
        human.setPosition(hr.x, hr.y);
        camx = human.getX() - 180;
        camy = human.getY() - 180;
    }
    
    private void clickScreen() {    //@TODO: prettier waitscreen
        boolean enterheld = commands[MouseEvent.BUTTON1];
        boolean cont = true;
        while (cont && stopFlag) {
            if (enterheld) {
                if (!commands[MouseEvent.BUTTON1])
                    enterheld = false;
            }
            
            if (!enterheld && commands[MouseEvent.BUTTON1])
                cont = false;
            
            ogr = buffer.createGraphics();
            ogr.setColor(Color.black);
            ogr.fillRect(0, 0, 400, 400);
            ogr.setColor(Color.white);
            ogr.setFont(new Font("Pause", Font.BOLD, 20));
            ogr.drawString("Level " + level, 150, 150);
            ogr.drawString("Click to Continue", 100, 200);
            ogr.dispose();
            this.getGraphics().drawImage(buffer, 0, 0, null);
        }
    }
    
    private void offPaint(long bdt) {
        //Paint offscreen
        ogr = buffer.createGraphics();
        ogr.setColor(Color.white);
        ogr.fillRect(0, 0, 400, 400);   //Clear offscreen image
        ogr.drawImage(bgimage.getSubimage(camx, camy, 400, 400), 0, 0, null);   //paint map
        human.draw(ogr, human.getX() - camx, human.getY() - camy);
        for (ComputerPlayer cp : cpuPlayers) {  
            cp.draw(ogr, cp.getX() - camx, cp.getY() - camy);
        }
        if (bdt > 50) { //animate powerups
            for (Powerup p : powerups) {
                if ((p.getX() >= camx && p.getX() <= camx+400) && (p.getY() >= camy && p.getY() <= camy+400))
                    p.mutate(15);
            }
        }
        for (Powerup p : powerups) {    //draw powerups
            if ((p.getX() >= camx && p.getX() <= camx+400) && (p.getY() >= camy && p.getY() <= camy+400))
                p.drawWithShift(ogr, -camx, -camy);
        }
        int totalbars = 1 + cpuPlayers.size();  //draw health bars
        ogr.setColor(Color.green);
        ogr.fillRect(400 - 20*totalbars, 10, 10, human.getHealth() / 2);
        ogr.setColor(Color.yellow);
        for (int i = 0; i < ammo; i++) {    //draw ammo bar
            ogr.fillRect(400 - 20*totalbars - 25, 5*i, 15, 3);
        }
        ogr.setColor(Color.orange);
        for (int i = 0; i < clips; i++) {
            ogr.fillRect(400 - 20*totalbars - 40, 15*i, 10, 10);
        }
        ogr.setColor(Color.red);
        for (int i = 0; i < cpuPlayers.size(); i++) {
            ogr.fillRect(400 - 20*(totalbars - i - 1), 10, 10, cpuPlayers.get(i).getHealth() / 2);
        }
        for (Bullet b : userbullets)    //drawing bullets
            b.drawWithShift(ogr, -camx, -camy);
        for (Bullet b : cpubullets)
            b.drawWithShift(ogr, -camx, -camy);
        double xrat = (double) camx / 1200.0;   //draw minimap and enemy dots
        double yrat = (double) camy / 1200.0;
        int xtop = (int)(xrat*75);
        int ytop = (int)(yrat*75);
        ogr.drawImage(minimap, 3, 3, null);
        ogr.setColor(new Color((float)0, (float)0, (float)1.0, (float).2));
        ogr.fillRect(3+xtop, 3+ytop, 25, 25);
        ogr.setColor(Color.red);
        for (ComputerPlayer cp : cpuPlayers) {
            xrat = (double)cp.getX() / 1200.0;
            yrat = (double)cp.getY() / 1200.0;
            xtop = (int)(xrat*75);
            ytop = (int)(yrat*75);

            ogr.fillRect(xtop-1, ytop-1, 3, 3);
        }
        for (ComputerPlayer cp : frozenCPU) {
            xrat = (double)cp.getX() / 1200.0;
            yrat = (double)cp.getY() / 1200.0;
            xtop = (int)(xrat*75);
            ytop = (int)(yrat*75);

            ogr.fillRect(xtop, ytop, 2, 2);

        }
        ogr.setColor(Color.yellow);
        ogr.setFont(new Font("Score", Font.BOLD, 20));
        ogr.drawString("" + score, 3, 398);
        if (ammo <= CLIP_SIZE / 4 && clips > 0) {
            ogr.setColor(Color.gray);
            ogr.drawString("R to Reload", 150, 225);
        }
        ogr.dispose();

        //flip the buffer
        this.getGraphics().drawImage(buffer, 0, 0, null);
        frames++;
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
        clickScreen();
        long loopController = System.currentTimeMillis();
        long bulletDelay = System.currentTimeMillis();
        long reloadDelay = System.currentTimeMillis();
        boolean reload = false;
        boolean pistolShot = false;
        gameloop: while(stopFlag) {
            long dt = System.currentTimeMillis() - loopController;
            if (dt < 4)
                continue gameloop;
            loopController = System.currentTimeMillis();
            
            long bdt = System.currentTimeMillis() - bulletDelay;
            if (bdt > 50)
                bulletDelay = System.currentTimeMillis();
            
            long rdt = System.currentTimeMillis() - reloadDelay;
            if (rdt > 500 && reload) {
                reload = false;
                if (clips > 0) {
                    ammo = CLIP_SIZE;
                    clips--;
                }
            }
            
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
            //@TODO: backwards movement logic for collision detections
            int umove = (int) (dt / 3);
            if (umove == 0) umove = (int)(Math.random() * 2);
            
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
            
            //reloading
            if (commands[KeyEvent.VK_R]) {
                if (!reload) {
                    reload = true;
                    reloadDelay = System.currentTimeMillis();
                }
            }
            
            //Start cpu movement logic
            //@TODO: same corrections to cpu collision detection
            int dx, dy;
            boolean inters = false;
            int cmove = (int)(dt / 5);
            if (cmove == 0) cmove = (int)(Math.random() * 2);
            
            cpumovement: for (ComputerPlayer cp : cpuPlayers) {
                if (cmove == 0)
                    break cpumovement;
                
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
            if (bdt > 50) {
                if (commands[MouseEvent.BUTTON1] && ammo > 0 && !reload) {
                    userbullets.add(human.shoot(mx+camx, my+camy));
                    ammo--;
                } else {    //empty ammo
                    if (commands[MouseEvent.BUTTON1] && !pistolShot) {
                        pistolShot = true;
                        userbullets.add(human.shoot(mx+camx, my+camy));
                    } else if (! commands[MouseEvent.BUTTON1]) {
                        pistolShot = false;
                    }
                }
                for (ComputerPlayer cp : cpuPlayers) {
                    if (Math.random() < .4 + level*.1)
                        cpubullets.add(cp.shoot(human.getX(), human.getY()));
                }
            }
            
            //Acquiring powerups
            for (int i = powerups.size() - 1; i >= 0; i--) {
                Powerup pu = powerups.get(i);
                if (human.getBounds().intersects(pu.getBounds())) {
                    switch (pu.getType()) {
                        case AMMO:
                            ammo += PowerupType.AMMO.getData();
                            clips+=3;
                            break;
                        case HEALTH:
                            human.heal(PowerupType.HEALTH.getData());
                            break;
                        case DAMAGE:
                            bulletdamage += 10;
                            break;
                    }
                    powerups.remove(i);
                    int placecounter = 0;
                    int placex, placey;
                    placecpu: while (placecounter < level) {    //spawn in more enemies
                        placex = (int) (Math.random() * GRID_SIZE);
                        placey = (int) (Math.random() * GRID_SIZE);

                        if (map[placex][placey])
                            continue placecpu;

                        frozenCPU.add(new ComputerPlayer(placex*20, placey*20));
                        placecounter++;
                    }
                    break;
                }
            }
            
            //Moving bullets
            for (Bullet b : userbullets)
                b.move(dt);
            for (Bullet b : cpubullets)
                b.move(dt);
            
            //checking bullet collisions and bounds
            inuserbullets: for (int i = userbullets.size() - 1; i >= 0; i--) {
                for (Polygon p : barriers) {
                    if (p.intersects(userbullets.get(i).getBounds())) {
                        userbullets.remove(i);
                        continue inuserbullets;
                    }
                }
                
                int bx = userbullets.get(i).getX();
                int by = userbullets.get(i).getY();
                if (bx < 0 || bx > DIM || by < 0 || by > DIM) {
                    userbullets.remove(i);
                    continue inuserbullets;
                }
                
                for (ComputerPlayer cp : cpuPlayers) {
                    if (userbullets.get(i).getBounds().intersects(cp.getBounds())) {
                        userbullets.remove(i);
                        cp.damage(bulletdamage);
                        continue inuserbullets;
                    }
                }
            }
            
            for (int i = cpuPlayers.size() - 1; i >= 0; i--) {
                if (cpuPlayers.get(i).getHealth() <= 0) {
                    cpuPlayers.remove(i);
                    score += 10 + (int)(1.5*level*Math.random());
                }
            }
            
            //checking cpu bullet collisions and bounds
            incpubullets: for (int i = cpubullets.size() -1; i >= 0; i--) {
                for (Polygon p : barriers) {
                    if (p.intersects(cpubullets.get(i).getBounds())) {
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
                
                if (cpubullets.get(i).getBounds().intersects(human.getBounds())) {
                    cpubullets.remove(i);
                    human.damage((level/2) + 1);
                }
            }
            
            //cpu collision with player
            if (bdt > 50) {
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
            
            //Freezing and unfreezing cpu players
            int cpx, cpy;
            for (int i = frozenCPU.size() - 1; i >= 0; i--) {
                cpx = frozenCPU.get(i).getX();
                cpy = frozenCPU.get(i).getY();
                
                if ((cpx >= camx && cpx <= camx+400) && (cpy >= camy && cpy <= camy+400))
                    cpuPlayers.add(frozenCPU.remove(i));
            }
            
            for (int i = cpuPlayers.size() - 1; i >= 0; i--) {
                cpx = cpuPlayers.get(i).getX();
                cpy = cpuPlayers.get(i).getY();
                
                if ((cpx < camx || cpx > camx+400) || (cpy < camy || cpy > camy+400))
                    frozenCPU.add(cpuPlayers.remove(i));
            }
                
            offPaint(bdt);  //Paint!
            
            if (human.getHealth() <= 0) {
                System.out.println("You lost"); //@TODO: something prettier
                long time2 = System.currentTimeMillis();
                stopFlag = false;
                int framerate =  (int) (frames / ((time2 - time) / 1000));
                System.out.println("Average " + framerate + "fps");
                System.exit(0);
            }
            
            if (cpuPlayers.isEmpty() && frozenCPU.isEmpty()) {
                System.out.println("New level");
                level++;
                score += 100;
                generateGame(true);
                clickScreen();
                time = System.currentTimeMillis();
                frames = 0;
            }
            
            boolean newlevel = false;
            if (commands[KeyEvent.VK_P] && !paused) {
                paused = true;
                commands[KeyEvent.VK_P] = false;
                while (paused) {
                    ogr = pbuffer.createGraphics();
                    ogr.setColor(Color.white);
                    ogr.fillRect(0, 0, 400, 400);

                    ogr.drawImage(buffer, 0, 0, null);
                    ogr.setColor(new Color((float)0.0, (float)0.0, (float)0.0, (float).5));
                    ogr.fillRect(0, 0, 400, 400);
                    
                    ogr.setColor(Color.white);
                    ogr.setFont(new Font("Pause", Font.BOLD, 20));
                    if ((my > 125 && my < 150))
                        ogr.setColor(Color.yellow);
                    ogr.drawString("Resume", 150, 150);
                    
                    ogr.setColor(Color.white);
                    if ((my > 175 && my < 200))
                        ogr.setColor(Color.yellow);
                    ogr.drawString("New Level", 140, 200);
                    
                    ogr.setColor(Color.white);
                    if ((my > 225 && my < 250))
                        ogr.setColor(Color.yellow);
                    ogr.drawString("Quit", 165, 250);
                    
                    if (commands[MouseEvent.BUTTON1]) {
                        if ((my > 125 && my < 150))
                            paused = false;
                        else if ((my > 175 && my < 200)) {
                            paused = false;
                            newlevel = true;
                        } else if ((my > 225 && my < 250)) {
                            paused = false;
                            stopFlag = false;
                            System.exit(0);
                        } 
                    }
                    
                    if (commands[KeyEvent.VK_P])
                        paused = false;
                    else if (commands[KeyEvent.VK_N]) {
                        paused = false;
                        newlevel = true;
                    } else if (commands[KeyEvent.VK_Q]) {
                        paused = false;
                        stopFlag = false;
                        System.exit(0);
                        //@TODO: more graceful exit
                    }
                    
                    if (newlevel) {
                        generateGame(false);
                        score -= 200;
                        clickScreen();
                        time = System.currentTimeMillis();
                    }
                    
                    getGraphics().drawImage(pbuffer, 0, 0, null);
                    ogr.dispose();
                }
                commands[MouseEvent.BUTTON1] = false;
                commands[KeyEvent.VK_P] = false;
                loopController = System.currentTimeMillis();
                time = System.currentTimeMillis();
                frames = 0;
            }
        }
    }
    
    @Override
    public void stop() {
        long time2 = System.currentTimeMillis();
        stopFlag = false;
        paused = false;
        try {
            gameLoop.join();
        } catch (InterruptedException ex) { }
        gameLoop = null;
        
        disableEvents(AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
        int framerate;
        try {
            framerate =  (int) (frames / ((time2 - time) / 1000));
        } catch (ArithmeticException ex) {
            framerate = 0;
        }
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
