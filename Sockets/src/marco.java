import javax.swing.*;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class marco extends JFrame implements Runnable {
    public marco() {
        setVisible(true); // Hago visible el marco 

        Thread mihilo = new Thread(this); // Nuevo hilo

        mihilo.start(); // Inicializo el hilo 
    }

    @Override
    public void run() {

        try {
            ServerSocket server1 = new ServerSocket(9995); // Creo el server1 el cual corresponde al socket con puerto 9995

            Socket tablero = server1.accept(); // Tablero de tipo socket pasa a guardar la respuesta aceptada 

            DataInputStream input = new DataInputStream(tablero.getInputStream()); // Se guarda en input el stream ingresado

            String salida = input.readUTF();

            input.close(); // Cierro la conexion 

            Socket new_tablero = new Socket("192.168.56.1",9990); // Establezco un nuevo socket con la direccion ip de la computadora y el puerto

            DataOutputStream output = new DataOutputStream(new_tablero.getOutputStream());// Se guarda en output el stream a mandar por medio del socket

            output.writeUTF(salida); // escribo en output el string salida 

            if(salida.equals("EXIT")){
                System.exit(0); // Si salida corresponde a exit cierro las conexiones y termina de ejecutarse
            }
            output.close(); // Cierro la conexion

        } catch (IOException e) {
            e.printStackTrace(); // En caso de tener algun error presente en la conexion
        }
        try {
            Socket moverP = new Socket("192.168.56.1",2000); // Se genera un nuevo socket con la direccion ip de la computadora y el puerto para transmitir el movimiento a ejecutar, 

            DataOutputStream output = new DataOutputStream(moverP.getOutputStream()); // Se guarda en output el stream a mandar por medio del socket este indica el movimiento

            ServerSocket server2 = new ServerSocket(9992); //// Creo el server1 el cual corresponde al socket con puerto 9992

            Socket mov = server2.accept(); // Acepta la conexion

            DataInputStream input = new DataInputStream(mov.getInputStream()); // Se guarda en input el stream ingresado

            String respuesta = input.readUTF();  

            input.close(); // Cierro la conexion

            output.writeUTF(respuesta); // Escribo la respuesta en el output

            output.close(); // Cierro la conexion

        } catch (IOException e) {
            e.printStackTrace();  // En caso de tener algun error presente en la conexion
        }

    }
}
