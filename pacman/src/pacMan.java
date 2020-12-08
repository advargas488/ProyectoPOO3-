import javax.swing.*;
import java.awt.*;

public class pacMan extends JFrame {
    public pacMan() {
        add(new module());

    }
    public static void main(String[] args) {
        Image up = new ImageIcon("pr.png").getImage();
        pacMan pac = new pacMan();
        pac.setVisible(true);
        pac.setTitle("Pacman");
        pac.setIconImage(up);
        pac.setSize(380,425);
        pac.setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
}
