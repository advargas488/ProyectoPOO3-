import javax.swing.*;
import java.awt.*;

public class Crossy_Road extends JFrame {
    public Crossy_Road( ) {
        add(new module());
    }
    public static void main(String[] args) {
        Image up = new ImageIcon("f.png").getImage();
        Crossy_Road pac = new Crossy_Road();
        pac.setVisible(true);
        pac.setTitle("Crossy Road");
        pac.setIconImage(up);
        pac.setSize(370,398);
        pac.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
