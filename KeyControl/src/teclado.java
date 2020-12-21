import javax.swing.*;

public class teclado extends JFrame {
    public teclado(){
        add(new crearTeclado());
    }
    public static void main(String[] args) {
        teclado one = new teclado();
        one.setVisible(true);
    }
}
