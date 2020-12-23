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
            ServerSocket server1 = new ServerSocket(9995);

            Socket tablero = server1.accept();

            DataInputStream input = new DataInputStream(tablero.getInputStream());

            String salida = input.readUTF();

            input.close();

            Socket new_tablero = new Socket("192.168.56.1",9990);

            DataOutputStream output = new DataOutputStream(new_tablero.getOutputStream());

            output.writeUTF(salida);

            if(salida.equals("EXIT")){
                System.exit(0);
            }
            output.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ServerSocket server2 = new ServerSocket(9991);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
