import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private final int CASILLA = 24;
    private final int N_CASILLAS = 15;
    private final int SCREEN = N_CASILLAS * CASILLA;
    private char [] screenData ={
            99,99,99,99,99,99,99,99,99,99,99,99,99,99,99
    }
    public Main(char [] tablero){

    }
    public static void main(String[] args){
        Main myFrame = new Main();
        myFrame.setVisible(true);
        myFrame.setSize(370,398);
    }

    private void drawMaze(Graphics2D g2d) {
        short i = 0;
        int x, y;

        for (y = 0; y < SCREEN; y += CASILLA) {
            for (x = 0; x < SCREEN; x += CASILLA) {
                g2d.setColor(new Color(0,100,0));
                g2d.setStroke(new BasicStroke(5));

                if ((screenData[i] == 2) || screenData[i] == 1) {
                    g2d.fillRect(x, y, CASILLA, CASILLA);
                }
                if ((screenData[i] == 3)) {
                    g2d.setColor(new Color(255,255,255));
                    g2d.drawLine(x, y, x+8 , y );
                }
                i++;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.gray);
        g2d.fillRect(0, 0, d.width, d.height);

        drawMaze(g2d);

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }
}
