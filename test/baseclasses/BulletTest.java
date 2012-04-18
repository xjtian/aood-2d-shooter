/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

import java.awt.BorderLayout;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author jacky
 */
public class BulletTest {
    /**
     * Test of move method, of class Bullet.
     */
    @Test
    public void testMove() {
        System.out.println("Move and Draw");
        final Bullet b = new Bullet(90, 300, 100, 290, false);
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JComponent c = new JComponent() {
            public void paintComponent(Graphics g) {
                b.draw(g);
            }
        };
        frame.add(c, BorderLayout.CENTER);
        frame.setSize(400, 400);
        frame.setVisible(true);
        while(frame.isVisible()) {
            b.move();
            c.repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {}
        }
        assert true;
    }

    /**
     * Test of draw method, of class Bullet.
     */
    @Test
    @Ignore
    public void testDraw() {
    }
}
