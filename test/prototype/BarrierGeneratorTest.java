/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.junit.Test;

/**
 *
 * @author xtian8741
 */
public class BarrierGeneratorTest {

    /**
     * Test of drawShapes method, of class BarrierGenerator.
     */
    @Test
    public void testDrawShapes() {
        System.out.println("drawShapes");
        
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JComponent c = new JComponent() {
          @Override
          public void paintComponent(Graphics g)   {
              BufferedImage ogr = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
              Graphics g2 = ogr.getGraphics();
              g2.setColor(Color.blue);
              g2.fillRect(0, 0, 800, 800);
              g2.setColor(Color.red);
              Rectangle[] stuff = BarrierGenerator.drawShapes(12, 800);
              for (Rectangle r : stuff) {
                  g2.fillRect(r.x, r.y, r.width, r.height);
              }
              
              g.drawImage(ogr, 0, 0, null);
          }
        };
        frame.add(c, BorderLayout.CENTER);
        frame.setSize(800, 800);
        frame.setVisible(true);
        while (frame.isVisible()) {
            frame.repaint();
        }
        
        assert true;
    }
    
    /**
     * Test of generateMap method, of class BarrierGenerator.
     */
    @Test
    public void testGenerateMap() {
        System.out.println("generateMap");
        int polygons = 12;
        int dimension = 800;
        
        final BufferedImage bi = new BufferedImage(800, 800, BufferedImage.TYPE_INT_RGB);
        int[] pixels = ((DataBufferInt) bi.getRaster().getDataBuffer()).getData();
        
        int[] back = BarrierGenerator.generateMap(polygons, dimension);
        System.arraycopy(back, 0, pixels, 0, back.length);
        
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        JComponent c = new JComponent() {
          @Override
          public void paintComponent(Graphics g)   {
              g.drawImage(bi, 0, 0, null);
          }
        };
        frame.add(c, BorderLayout.CENTER);
        frame.setSize(800, 800);
        frame.setVisible(true);
        while (frame.isVisible()) {
            back = BarrierGenerator.generateMap(polygons, dimension);
            System.arraycopy(back, 0, pixels, 0, back.length);
            frame.repaint();
        }
        
        assert true;
    }
}
