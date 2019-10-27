import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphPanel extends JPanel {

    BufferedImage graph;

    public GraphPanel(BufferedImage image) {
        graph = image;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(graph, 0, 0, null);

    }

    public void refreshGraph(BufferedImage g) {
        graph = g;
        this.repaint();
    }

}
