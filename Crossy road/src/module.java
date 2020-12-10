import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class module extends JPanel implements ActionListener {
    private Dimension d;
    private boolean inGame = false;//para saber si el jugador quiere jugar o no
    private boolean muerto = false;
    private int lives;
    private int N_C = 4;//numero de enemigos
    private int[] c_x, c_y, c_dx, c_dy, cSpeed;
    //variables para algunas operaciones y para definir el tamano de la ventana
    private final int CASILLA = 24;
    private final int N_CASILLAS = 15;
    private final int SCREEN = N_CASILLAS * CASILLA;
    private final int MAX_C = 6;
    private final int F_SPEED = 12;

    private int frog_x, frog_y, frog_dx,frog_dy;//x y y son coordenadas, dx y dy para direccionamiento

    private Image frog,car;

    private int req_dx, req_dy;//para el movimiento del jugador

    private final short levelData[] = {
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
            1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,
            2,2,2,2,2,2,2,2,2,2,2,2,2,2,2
    };

    private final int[] validSpeeds = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 6;

    private int currentSpeed = 3;
    private short[] screenData;
    private Timer timer;

    public module() {
        loadImages();
        initVariables();
        addKeyListener(new TAdapter());
        setFocusable(true);
        initGame();
    }

    private void checkGame(Graphics2D g2d){

        if(frog_y == 1){
            String win = "Has ganado!";
            g2d.setColor(Color.yellow);
            g2d.drawString(win, (SCREEN)/4 - 30, 125);
            inGame = false;
        }
    }
    private void loadImages(){
        frog = new ImageIcon("f.png").getImage();
        car = new ImageIcon("c.png").getImage();
    }

    private void initVariables(){
        screenData = new short[N_CASILLAS * CASILLA];
        d = new Dimension(370, 398);
        c_x = new int[MAX_C];
        c_dx = new int[MAX_C];
        c_y = new int[MAX_C];
        c_dy = new int[MAX_C];
        cSpeed = new int[MAX_C];

        timer = new Timer(40, this);
        timer.start();
    }

    private void playGame(Graphics2D g2d) {
        if (muerto) {
            death();
        } else {
            moveFrog();
            paintFrog(g2d);
            moveCar(g2d);
            checkMaze();
            checkGame(g2d);
        }
    }

    private void showIntroScreen(Graphics2D g2d) {

        String start = "Presione ESPACIO para empezar!";
        g2d.setColor(Color.yellow);
        g2d.drawString(start, (SCREEN)/4 - 30, 125);

    }

    private void checkMaze() {
        int i = 0;
        boolean finished = true;

        while (i < N_CASILLAS * N_CASILLAS && finished) {

            if ((screenData[i]) != 0) {
                finished = false;
            }

            i++;
        }

        if (finished) {

            if (N_C < MAX_C) {
                N_C++;
            }

            if (currentSpeed < maxSpeed) {
                currentSpeed++;
            }

            initTablero();
        }
    }

    private void death() {

        lives--;

        if (lives == 0) {
            inGame = false;
        }

        continueJugar();
    }

    private void moveCar(Graphics2D g2d) {
        for (int i = 0; i < N_C; i++) {
            c_x[i] = c_x[i] + 1;
            c_x[i] = c_x[i] + cSpeed[i];
            if(c_x[i] > 15 * CASILLA)
                c_x[i] = CASILLA;
            drawCar(g2d, c_x[i] + 1, c_y[i] + 1);
            if (frog_x > (c_x[i] - 12) && frog_x < (c_x[i] + 12)
                    && frog_y > (c_y[i] - 12) && frog_y < (c_y[i] + 12)
                    && inGame) {
                muerto = true;
            }
        }
    }

    private void moveFrog(){
        if (req_dx != 0 || req_dy != 0) {
            frog_x += req_dx;
            frog_y += req_dy;
        }
        frog_x = frog_x + (F_SPEED+10) * frog_dx;
        frog_y = frog_y + (F_SPEED+10) * frog_dy;
    }

    private void paintFrog(Graphics2D g2d){
        if (req_dx == -1) {
            g2d.drawImage(frog, frog_x + 1, frog_y + 1, this);
        } else if (req_dx == 1) {
            g2d.drawImage(frog, frog_x + 1, frog_y + 1, this);
        } else if (req_dy == -1) {
            g2d.drawImage(frog, frog_x + 1, frog_y + 1, this);
        } else {
            g2d.drawImage(frog, frog_x + 1, frog_y + 1, this);
        }
    }

    private void drawCar(Graphics2D g2d, int x, int y){
        g2d.drawImage(car,x , y, this);
    }

    private void drawMaze(Graphics2D g2d) {
        short i = 0;
        int x, y;

        for (y = 0; y < SCREEN; y += CASILLA) {
            for (x = 0; x < SCREEN; x += CASILLA) {
                g2d.setColor(new Color(0,100,0));
                g2d.setStroke(new BasicStroke(5));

                if ((levelData[i] == 2) || levelData[i] == 1) {
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

    private void initGame() {
        lives = 3;
        initTablero();
        N_C = 6;
        currentSpeed = 3;
    }

    private void initTablero() {

        int i;
        for (i = 0; i < N_CASILLAS * N_CASILLAS; i++) {
            screenData[i] = levelData[i];
        }

        continueJugar();
    }

    private void continueJugar() {
        int dx = 1;
        int random;

        for (int i = 0; i < N_C; i++) {

            c_y[i] = ((i+1) * CASILLA)*2; //posicion inicial
            c_x[i] = 2 * CASILLA;
            c_dy[i] = 0;
            c_dx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (currentSpeed + 1));

            if (random > currentSpeed) {
                random = currentSpeed;
            }

            cSpeed[i] = validSpeeds[random];
        }

        frog_x = 8 * CASILLA;  //start position
        frog_y = 14 * CASILLA;
        frog_dx = 0;	//reset direction move
        frog_dy = 0;
        req_dx = 0;		// reset direction controls
        req_dy = 0;
        muerto = false;

    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.gray);
        g2d.fillRect(0, 0, d.width, d.height);

        drawMaze(g2d);
        if (inGame) {
            playGame(g2d);
        } else {
            showIntroScreen(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    //controls
    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (inGame) {
                if (key == KeyEvent.VK_A) {
                    req_dx = -1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_D) {
                    req_dx = 1;
                    req_dy = 0;
                } else if (key == KeyEvent.VK_W) {
                    req_dx = 0;
                    req_dy = -1;
                } else if (key == KeyEvent.VK_S) {
                    req_dx = 0;
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false;
                }
            } else {
                if (key == KeyEvent.VK_SPACE) {
                    inGame = true;
                    initGame();
                }
            }
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
