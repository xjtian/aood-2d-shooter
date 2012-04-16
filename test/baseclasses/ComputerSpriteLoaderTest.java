/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package baseclasses;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author jacky
 */
public class ComputerSpriteLoaderTest {

    /**
     * Test of loadAllImages method, of class ComputerSpriteLoader.
     */
    @Test
    @Ignore
    public void testLoadAllImages() {
    }

    /**
     * Test of getSprite method, of class ComputerSpriteLoader.
     */
    @Test
    public void testGetSprite() {
        System.out.println("Get Sprite");
        ComputerSpriteLoader.loadAllImages();
        
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JComponent c = new JComponent() {
            @Override
            public void paintComponent(Graphics g) {
                for (int d = 0; d < 360; d+=15) {
                    int index = d / 15;
                    
                    int gx = index % 6;
                    int gy = index / 6;
                    
                    g.drawImage(ComputerSpriteLoader.getSprite(d), gx*40, gy*40, null);
                }
            }
        };
        
        frame.setLayout(new BorderLayout());
        c.setPreferredSize(new Dimension(300, 300));
        frame.add(c, BorderLayout.CENTER);
        frame.pack();
        
        frame.setVisible(true);
        while (frame.isVisible()) {}
        assert true;
    }
}
