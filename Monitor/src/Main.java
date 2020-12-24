import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class main extends JFrame {


    public main( ) {
        add(new screen());

    }
    public static void main(String[] args){
        main pantalla = new main();
        pantalla.setVisible(true);
        pantalla.setSize(380,425);
        pantalla.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

}
