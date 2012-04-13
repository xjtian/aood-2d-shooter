/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Polygon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author jacky
 */
public class BarrierFactoryTest {

    /**
     * Test of setGridSize method, of class BarrierFactory.
     */
    @Ignore
    @Test
    public void testSetGridSize() {
        
    }

    /**
     * Test of generateRectangleBlock method, of class BarrierFactory.
     */
    @Test
    public void testGenerateRectangleBlock() {
        System.out.println("Rectangle block");
        final Polygon p = BarrierFactory.generateRectangleBlock(2, 2, 4, 2);
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JComponent c = new JComponent() {
            public void paintComponent(Graphics g) {
                g.fillPolygon(p);
            }
        };
        frame.add(c, BorderLayout.CENTER);
        frame.setSize(200, 200);
        frame.setVisible(true);
        while(frame.isVisible()) {}
        assert true;
    }

    /**
     * Test of generateLeftLBlock method, of class BarrierFactory.
     */
    @Test
    public void testGenerateLeftLBlock() {
        System.out.println("Left L block");
        final Polygon p = BarrierFactory.generateLeftLBlock(2, 2, 4, 2);
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JComponent c = new JComponent() {
            public void paintComponent(Graphics g) {
                g.fillPolygon(p);
            }
        };
        frame.add(c, BorderLayout.CENTER);
        frame.setSize(200, 200);
        frame.setVisible(true);
        while(frame.isVisible()) {}
        assert true;
    }

    /**
     * Test of generateRightLBlock method, of class BarrierFactory.
     */
    @Test
    public void testGenerateRightLBlock() {
        System.out.println("Right L block");
        final Polygon p = BarrierFactory.generateRightLBlock(2, 2, 4, 2);
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JComponent c = new JComponent() {
            public void paintComponent(Graphics g) {
                g.fillPolygon(p);
            }
        };
        frame.add(c, BorderLayout.CENTER);
        frame.setSize(200, 200);
        frame.setVisible(true);
        while(frame.isVisible()) {}
        assert true;
    }
}
