import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;



public class crearTeclado extends JPanel implements ActionListener {
    public crearTeclado(){
        addKeyListener(new TAdapter());
        setFocusable(true);
    }

    class TAdapter extends KeyAdapter {
        String juegoActual = "INICIO";
        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();
            System.out.println(key - '0');
            if (juegoActual == "INICIO") {
                if (key == KeyEvent.VK_1) {
                    juegoActual = "CROSSY";
                    try {
                        Socket tablero = new Socket("192.168.56.1", 9995);

                        DataOutputStream output = new DataOutputStream(tablero.getOutputStream());

                        output.writeUTF("CROSS");

                        output.close();

                    } catch (IOException ioException) {
                        System.out.println(ioException.getMessage());
                    }
                } else if (key == KeyEvent.VK_2) {
                    juegoActual = "PACMAN";
                    try {
                        Socket tablero = new Socket("192.168.56.1", 9995);

                        DataOutputStream output = new DataOutputStream(tablero.getOutputStream());

                        output.writeUTF("PAC");

                        output.close();

                    } catch (IOException ioException) {
                        System.out.println(ioException.getMessage());
                    }
                } else if (key == KeyEvent.VK_3){
                    try {
                        Socket tablero = new Socket("192.168.56.1", 9995);

                        DataOutputStream output = new DataOutputStream(tablero.getOutputStream());

                        output.writeUTF("EXIT");

                        output.close();

                    } catch (IOException ioException) {
                        System.out.println(ioException.getMessage());
                    }
                    System.exit(0);
                }
            }
            if (juegoActual == "PACMAN" || juegoActual == "CROSS"){
                if(key == KeyEvent.VK_W){
                    Socket mov = null;
                    try {
                        mov = new Socket("192.168.56.1", 9992);

                        DataOutputStream output = new DataOutputStream(mov.getOutputStream());

                        output.writeUTF("UP");

                        output.close();

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }

                }
                else if(key == KeyEvent.VK_A) {
                    Socket mov = null;
                    try {
                        mov = new Socket("192.168.56.1", 9992);

                        DataOutputStream output = new DataOutputStream(mov.getOutputStream());

                        output.writeUTF("LEFT");

                        output.close();

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                else if(key == KeyEvent.VK_S) {
                    Socket mov = null;
                    try {
                        mov = new Socket("192.168.56.1", 9992);

                        DataOutputStream output = new DataOutputStream(mov.getOutputStream());

                        output.writeUTF("DOWN");

                        output.close();

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                else if(key == KeyEvent.VK_D) {
                    Socket mov = null;
                    try {
                        mov = new Socket("192.168.56.1", 9992);

                        DataOutputStream output = new DataOutputStream(mov.getOutputStream());

                        output.writeUTF("RIGHT");

                        output.close();

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                else if(key == KeyEvent.VK_ESCAPE) {
                    Socket mov = null;
                    try {
                        mov = new Socket("192.168.56.1", 9992);

                        DataOutputStream output = new DataOutputStream(mov.getOutputStream());

                        output.writeUTF("BACK");

                        output.close();

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    juegoActual = "INICIO";
                }
                else if(key == KeyEvent.VK_SPACE){
                    Socket mov = null;
                    try {
                        mov = new Socket("192.168.56.1", 9992);

                        DataOutputStream output = new DataOutputStream(mov.getOutputStream());

                        output.writeUTF("INICIAR");

                        output.close();

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
