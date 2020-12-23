import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class module extends JPanel implements ActionListener {

    private Dimension d;
    private final Font font = new Font("Arial", Font.BOLD, 14);
    private boolean inGame = false;
    private boolean muerto = false;
    //variables para mover a los enemigos
    private int N_G = 4;//numero de enemigos al inicio
    private int lives, score;
    private int[] dx, dy;
    private int[] g_x, g_y, g_dx, g_dy, gSpeed;
    //variables para algunas operaciones y para definir el tamano de la ventana
    private final int CASILLA = 24;
    private final int N_CASILLAS = 15;
    private final int SCREEN = N_CASILLAS * CASILLA;
    private final int MAX_G = 9;
    private final int P_SPEED = 6;

    private Image ghost;
    private Image up, down, left, right;

    private int pacman_x, pacman_y, pacmand_x, pacmand_y;//x y y son coordenadas, dx y dy para direccionamiento
    private int req_dx, req_dy;

    private final short levelData[] = {
            //para las casillas con un punto se agrega un valor 16
            //para las casillas con borde arriba un 2
            //para las casillas con borde a la izquierda un 1
            //para las casillas con borde a la derecha un 4
            //para las casillas con borde a abajo un 8
            //para las casillas que son puntos para pacman, un 16
            //los valores se suman, esto para que pacman ni los enemigos traspasen muros
            19,26,26,18,26,26,26,18,26,18,26,26,26,18,30,
            21,0 ,0 ,21,0 ,0, 0, 21,0, 21, 0 ,0 ,0 ,21,0,
            21,0, 0 ,21,0 ,0 ,0 ,21,0 ,21, 0 ,0 ,0 ,21,0,
            17,26,26,16,26,18,26,24,26,24,26,18,26,24,22,
            21,0 ,0 ,21,0 ,21,0 ,0 ,0 ,0 ,0 ,21,0 ,0 ,21,
            17,26,26,20,0 ,25,26,22,0 ,19,26,28,0 ,0 ,21,
            21,0 ,0 ,21,0 ,0 ,0 ,21,0 ,21,0 ,0 ,0 ,0 ,21,
            21,0 ,0 ,21,0 ,19,18,16,18,16,26,26,26,26,20,
            21,0 ,0 ,21,0 ,17,16,16,16,20,0 ,0 ,0 ,0 ,21,
            21,0 ,0 ,17,26,16,16,16,16,16,26,26,22,0 ,21,
            21,0 ,0 ,21,0 ,17,16,16,16,20,0 ,0 ,21,0 ,21,
            17,26,26,20,0 ,17,24,24,24,20,0 ,0 ,21,0 ,21,
            21,0 ,0 ,21,0 ,21,0 ,0 ,0 ,17,26,26,24,26,20,
            21,0 ,0 ,21,0 ,21,0 ,0 ,0 ,21,0 ,0 ,0 ,0 ,21,
            25,26,26,24,26,24,26,26,26,24,26,26,26,26,28,
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


    private void loadImages() {
        down = new ImageIcon("pd.png").getImage();
        up = new ImageIcon("pu.png").getImage();
        left = new ImageIcon("pl.png").getImage();
        right = new ImageIcon("pr.png").getImage();
        ghost = new ImageIcon("g.png").getImage();

    }
    private void initVariables() {

        screenData = new short[N_CASILLAS * CASILLA];
        d = new Dimension(400, 400);
        g_x = new int[MAX_G];
        g_dx = new int[MAX_G];
        g_y = new int[MAX_G];
        g_dy = new int[MAX_G];
        gSpeed = new int[MAX_G];
        dx = new int[4];
        dy = new int[4];

        timer = new Timer(40, this);
        timer.start();
    }

    private void playGame(Graphics2D g2d) {

        if (muerto) {

            death();

        } else {

            movePacman();
            drawPacman(g2d);
            moveGhosts(g2d);
            checkMaze();
            checkWin(g2d);
        }
    }

    private void showIntroScreen(Graphics2D g2d) {
        String start = "Presione ESPACIO para empezar!";
        g2d.setColor(Color.yellow);
        g2d.drawString(start, (SCREEN)/4 - 30, 150);
    }

    private void checkWin(Graphics2D g2d){
        String win = "Has ganado!";

        if(score == 142){
            g2d.setColor(Color.yellow);
            g2d.drawString(win, SCREEN/4, 180);
            inGame = false;
        }
    }



    private void drawScore(Graphics2D g) {
        g.setFont(font);
        g.setColor(new Color(5, 181, 79));
        String s = "Score: " + score;
        g.drawString(s, SCREEN / 2 - 35 , SCREEN + 16);
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

            score += 50;

            if (N_G < MAX_G) {
                N_G++;
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

    private void moveGhosts(Graphics2D g2d) {

        int pos;
        int count;

        for (int i = 0; i < N_G; i++) {
            if (g_x[i] % CASILLA == 0 && g_y[i] % CASILLA == 0) {
                pos = g_x[i] / CASILLA + N_CASILLAS * (int) (g_y[i] / CASILLA);

                count = 0;

                if ((screenData[pos] & 1) == 0 && g_dx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 2) == 0 && g_dy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }

                if ((screenData[pos] & 4) == 0 && g_dx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 8) == 0 && g_dy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                if (count == 0) {

                    if ((screenData[pos] & 15) == 15) {
                        g_dx[i] = 0;
                        g_dy[i] = 0;
                    } else {
                        g_dx[i] = -g_dx[i];
                        g_dy[i] = -g_dy[i];
                    }

                } else {

                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    g_dx[i] = dx[count];
                    g_dy[i] = dy[count];
                }

            }

            g_x[i] = g_x[i] + (g_dx[i] * gSpeed[i]);
            g_y[i] = g_y[i] + (g_dy[i] * gSpeed[i]);
            drawGhost(g2d, g_x[i] + 1, g_y[i] + 1);

            if (pacman_x > (g_x[i] - 12) && pacman_x < (g_x[i] + 12)
                    && pacman_y > (g_y[i] - 12) && pacman_y < (g_y[i] + 12)
                    && inGame) {

                muerto = true;
            }
        }
    }

    private void drawGhost(Graphics2D g2d, int x, int y) {
        g2d.drawImage(ghost, x, y, this);
    }

    private void movePacman() {

        int pos;
        short ch;

        if (pacman_x % CASILLA == 0 && pacman_y % CASILLA == 0) {
            pos = pacman_x / CASILLA + N_CASILLAS * (int) (pacman_y / CASILLA);
            ch = screenData[pos];
            if ((ch & 16) != 0) {
                screenData[pos] = (short) (ch & 15);
                score++;
            }

            if (req_dx != 0 || req_dy != 0) {
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                }
            }

            // si sigue en la misma posicion
            if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
                    || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
                    || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
                    || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
                pacmand_x = 0;
                pacmand_y = 0;
            }
        }
        pacman_x = pacman_x + P_SPEED * pacmand_x;
        pacman_y = pacman_y + P_SPEED * pacmand_y;
    }

    private void drawPacman(Graphics2D g2d) {
        if (req_dx == -1) {
            g2d.drawImage(left, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dx == 1) {
            g2d.drawImage(right, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dy == -1) {
            g2d.drawImage(up, pacman_x + 1, pacman_y + 1, this);
        } else {
            g2d.drawImage(down, pacman_x + 1, pacman_y + 1, this);
        }
    }

    private void drawMaze(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 0; y < SCREEN; y += CASILLA) {
            for (x = 0; x < SCREEN; x += CASILLA) {

                g2d.setColor(new Color(0,72,251));
                g2d.setStroke(new BasicStroke(5));

                if ((levelData[i] == 0)) {
                    g2d.fillRect(x, y, CASILLA, CASILLA);
                }

                if ((screenData[i] & 1) != 0) {
                    g2d.drawLine(x, y, x, y + CASILLA - 1);
                }

                if ((screenData[i] & 2) != 0) {
                    g2d.drawLine(x, y, x + CASILLA - 1, y);
                }

                if ((screenData[i] & 4) != 0) {
                    g2d.drawLine(x + CASILLA - 1, y, x + CASILLA - 1,
                            y + CASILLA - 1);
                }

                if ((screenData[i] & 8) != 0) {
                    g2d.drawLine(x, y + CASILLA - 1, x + CASILLA - 1,
                            y + CASILLA - 1);
                }

                if ((screenData[i] & 16) != 0) {
                    g2d.setColor(new Color(255,255,255));
                    g2d.fillOval(x + 10, y + 10, 6, 6);
                }

                i++;
            }
        }
    }

    private void initGame() {
        lives = 3;
        score = 0;
        initTablero();
        N_G = 6;
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

        for (int i = 0; i < N_G; i++) {

            g_y[i] = 4 * CASILLA; //posicion inicial
            g_x[i] = 4 * CASILLA;
            g_dy[i] = 0;
            g_dx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (currentSpeed + 1));

            if (random > currentSpeed) {
                random = currentSpeed;
            }

            gSpeed[i] = validSpeeds[random];
        }

        pacman_x = 7 * CASILLA;  //start position
        pacman_y = 11 * CASILLA;
        pacmand_x = 0;	//reset direction move
        pacmand_y = 0;
        req_dx = 0;		// reset direction controls
        req_dy = 0;
        muerto = false;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);

        drawMaze(g2d);
        drawScore(g2d);

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
