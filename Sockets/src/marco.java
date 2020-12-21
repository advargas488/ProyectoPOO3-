import javax.swing.*;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class marco extends JFrame implements Runnable {
    public marco() {
        setVisible(true);

        Thread mihilo = new Thread(this);

        mihilo.start();
    }

    @Override
    public void run() {
        //System.out.println("I am listening");

        try {
            ServerSocket server1 = new ServerSocket(9999);

            Socket tablero = server1.accept();

            DataInputStream input = new DataInputStream(tablero.getInputStream());

            tablero.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
