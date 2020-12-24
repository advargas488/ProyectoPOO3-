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
    // Constructor para empezar a detectar caracteres del teclado
    public crearTeclado(){
        addKeyListener(new TAdapter());
        setFocusable(true);
    }
    // Clase Tadapter
    class TAdapter extends KeyAdapter {
        String juegoActual = "INICIO"; // String que indica en el juego que me encuentro actualmente
        @Override
        
        // E: Una tecla de entrada
        // S: Nada
        // Evalua los distintos casos dependiendo del valor de tecla ingresada
        public void keyPressed(KeyEvent e) {
            
            int key = e.getKeyCode(); // Codigo de tecla ingresada
            System.out.println(key - '0'); 
            
            // Casos del teclado cuando se esta en pantalla inicial 
            if (juegoActual == "INICIO") {
                if (key == KeyEvent.VK_1) {
                    juegoActual = "CROSSY"; // Abre crossy road al presionar el 1
                    try {
                        Socket tablero = new Socket("192.168.56.1", 9995);// Conexion 

                        DataOutputStream output = new DataOutputStream(tablero.getOutputStream()); //Indica mediante sockets a tablero que se inicio crossy road

                        output.writeUTF("CROSS");

                        output.close(); // Cierro la conexion

                    } catch (IOException ioException) {
                        System.out.println(ioException.getMessage()); // En caso de fallar la comunicacion 
                    }
                } else if (key == KeyEvent.VK_2) {
                    juegoActual = "PACMAN"; // Abre pacman al presionar el 2
                    try {
                        Socket tablero = new Socket("192.168.56.1", 9995);// Conexion 

                        DataOutputStream output = new DataOutputStream(tablero.getOutputStream());

                        output.writeUTF("PAC"); // Indica mediante sockets que se inicio Pacman a tablero

                        output.close();// Cierro la conexion

                    } catch (IOException ioException) {
                        System.out.println(ioException.getMessage()); // En caso de fallar la comunicacion 
                    }
                } else if (key == KeyEvent.VK_3){
                    try {
                        Socket tablero = new Socket("192.168.56.1", 9995);// Conexion 

                        DataOutputStream output = new DataOutputStream(tablero.getOutputStream()); // Indica mediante sockets a tablero que el juego 

                        output.writeUTF("EXIT");

                        output.close();// Cierro la conexion

                    } catch (IOException ioException) {
                        System.out.println(ioException.getMessage()); // En caso de fallar la comunicacion 
                    }
                    System.exit(0); // Finaliza todos los procesos ejecutandose
                }
            }
            if (juegoActual == "PACMAN" || juegoActual == "CROSS"){
                // Casos de teclado al ingresar dentro de un juego indicado en juegoActual
                // Se utilizan los mismos codigos de letras en ambos juegos
                if(key == KeyEvent.VK_W){
                    // Tecla W, y socket inicializado para indicar el movimiento
                    Socket mov = null;
                    try {
                        mov = new Socket("192.168.56.1", 9992); // Conexion 

                        DataOutputStream output = new DataOutputStream(mov.getOutputStream()); // Indica el movimiento del personaje hacia arriba mediante sockets al monitor 

                        output.writeUTF("UP");

                        output.close();// Cierro la conexion

                    } catch (IOException ioException) {
                        ioException.printStackTrace();// En caso de fallar la comunicacion 
                    }

                }
                else if(key == KeyEvent.VK_A) {
                    // Tecla A, y socket inicializado para indicar el movimiento
                    Socket mov = null;
                    try {
                        mov = new Socket("192.168.56.1", 9992);// Conexion 

                        DataOutputStream output = new DataOutputStream(mov.getOutputStream()); // Indica el movimiento del personaje hacia izquierda mediante sockets al monitor 

                        output.writeUTF("LEFT");

                        output.close();// Cierro la conexion

                    } catch (IOException ioException) {
                        ioException.printStackTrace();// En caso de fallar la comunicacion 
                    }
                }
                else if(key == KeyEvent.VK_S) {
                    // Tecla S, y socket inicializado para indicar el movimiento
                    Socket mov = null;
                    try {
                        mov = new Socket("192.168.56.1", 9992);// Conexion 

                        DataOutputStream output = new DataOutputStream(mov.getOutputStream()); // Indica el movimiento del personaje hacia abajo mediante sockets al monitor 

                        output.writeUTF("DOWN");

                        output.close();// Cierro la conexion

                    } catch (IOException ioException) {
                        ioException.printStackTrace();// En caso de fallar la comunicacion 
                    }
                }
                else if(key == KeyEvent.VK_D) {
                    // Tecla D, y socket inicializado para indicar el movimiento
                    Socket mov = null;
                    try {
                        mov = new Socket("192.168.56.1", 9992);// Conexion 

                        DataOutputStream output = new DataOutputStream(mov.getOutputStream()); // Indica el movimiento del personaje hacia la derecha mediante sockets al monitor 

                        output.writeUTF("RIGHT");

                        output.close();// Cierro la conexion

                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                else if(key == KeyEvent.VK_ESCAPE) {
                    // Tecla Escape, y socket inicializado para indicar el movimiento
                    Socket mov = null;
                    try {
                        mov = new Socket("192.168.56.1", 9992);// Conexion 

                        DataOutputStream output = new DataOutputStream(mov.getOutputStream()); // Indica que se regresa a la pantalla de menu al indicar escape

                        output.writeUTF("BACK");

                        output.close();// Cierro la conexion

                    } catch (IOException ioException) {
                        ioException.printStackTrace();// En caso de fallar la comunicacion 
                    }
                    juegoActual = "INICIO";
                }
                else if(key == KeyEvent.VK_SPACE){
                    // Tecla espaciadora, y socket inicializado para indicar el movimiento
                    Socket mov = null;
                    try {
                        mov = new Socket("192.168.56.1", 9992);// Conexion 

                        DataOutputStream output = new DataOutputStream(mov.getOutputStream()); // Indica que el juego se puede iniciar al monitor al darle a la barra espaciadora

                        output.writeUTF("INICIAR");

                        output.close(); // Cierro la conexion

                    } catch (IOException ioException) {
                        ioException.printStackTrace();// En caso de fallar la comunicacion 
                    }
                }
            }
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
