/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package prototype;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import javax.swing.JComponent;
import javax.swing.JFrame;
import org.junit.*;

/**
 *
 * @author xtian8741
 */
public class BarrierGeneratorTest {
    
    public BarrierGeneratorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of generateMap method, of class BarrierGenerator.
     */
    @Test
    public void testGenerateMap() {
        System.out.println("generateMap");
        int polygons = 4;
        int dimension = 200;
        
        final BufferedImage bi = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
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
        frame.setSize(200, 200);
        frame.setVisible(true);
        while (frame.isVisible()) {}
        
        assert true;
    }
}
