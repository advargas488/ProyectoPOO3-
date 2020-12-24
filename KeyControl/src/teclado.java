import javax.swing.*;

public class teclado extends JFrame {
    public teclado(){
        add(new crearTeclado());
    }
    // Hilo de teclado, y setvisible lo que permite la interaccion con este
    public static void main(String[] args) {
        teclado one = new teclado();
        one.setVisible(true);
        one.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
