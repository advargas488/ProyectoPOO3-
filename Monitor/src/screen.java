import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class screen extends JPanel implements Runnable, ActionListener {
    String fase = "INICIO";//para saber en cual juego se encuentra
    boolean inGame = false;//para cuando hay una entrada del usuario
    boolean pac_ingame = false;//para saber si ya se entro al juego
    boolean cross_ingame = false;
    //variables para el manejo de la pantalla y el posicionamiento
    private final int CASILLA = 24;
    private final int N_CASILLAS = 15;
    private final int SCREEN = N_CASILLAS * CASILLA;
    Dimension d = new Dimension(370, 398);
    private int [] screen_data = {//arreglo principal
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,
            0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
    };
    private final int crossy_data[] = {//arreglo del juego de crossy road
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

    private final int pac_data[] = {//arreglo del juego de pacman
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
    private boolean muerto = false;//para saber si esta muerto el personaje
    //variables para mover a los enemigos
    private int N_G = 4;//numero de enemigos al inicio
    private int livesP, score;
    private int[] dx, dy;
    private int[] g_x, g_y, g_dx, g_dy, gSpeed;
    //variables para algunas operaciones y para definir el tamano de la ventana
    private final int MAX_G = 9;//maximo de enemigos de pacman
    private final int P_SPEED = 6;//velocidad de pacman

    private Image ghost;
    private Image up, down, left, right;

    private int pacman_x, pacman_y, pacmand_x, pacmand_y;//x y y son coordenadas, dx y dy para direccionamiento
    private int req_dx, req_dy;
    //para las velocidades de los enemigos
    private final int[] validSpeeds = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 6;

    private int currentSpeed = 3;
    private Timer timer;

    private boolean muertoC = false;//para saber si el jugador sigue vivo
    private int livesC;//para el numero de vidas del jugador
    private int N_C = 4;//numero de carros
    private int[] c_x, c_y, c_dx, c_dy, cSpeed;
    private final int MAX_C = 6;//maximo de carros
    private final int F_SPEED = 12;//la velocidad de la rana

    private int frog_x, frog_y, frog_dx,frog_dy;//x y y son coordenadas, dx y dy para direccionamiento

    private Image frog,car;

    public screen(){
        initFrame();//inicia el programa
        initVariablesP();//inicia las variables
        loadImages();//carga las imagenes
        Thread mihilo = new Thread(this);//un hilo para ir pidiendo las entradas manejadas por sockets
        mihilo.start();

    }
    private void initVariablesP() {
        //se inician las variables con los valores para determinar el numero de enemigos y demas
        g_x = new int[MAX_G];
        g_dx = new int[MAX_G];
        g_y = new int[MAX_G];
        g_dy = new int[MAX_G];
        gSpeed = new int[MAX_G];
        dx = new int[4];
        dy = new int[4];

        c_x = new int[MAX_C];
        c_dx = new int[MAX_C];
        c_y = new int[MAX_C];
        c_dy = new int[MAX_C];
        cSpeed = new int[MAX_C];

        timer = new Timer(40, this);
        timer.start();
    }

    private void loadImages() {
        //se cargan las imagenes
        down = new ImageIcon("pd.png").getImage();
        up = new ImageIcon("pu.png").getImage();
        left = new ImageIcon("pl.png").getImage();
        right = new ImageIcon("pr.png").getImage();
        ghost = new ImageIcon("g.png").getImage();
        frog = new ImageIcon("f.png").getImage();
        car = new ImageIcon("c.png").getImage();
    }
    private void checkGame(Graphics2D g2d){
    //se fija si el jugador gano en crossy road
        if(frog_y == 1){
            String win = "Has ganado!";
            g2d.setColor(Color.yellow);
            g2d.drawString(win, (SCREEN)/4 - 30, 125);
            inGame = false;
        }
    }

    private void playCrossy(Graphics2D g2d) {
        //juega crossy road
        if (muerto) {
            deathC();
        } else {
            moveFrog();//mueve el jugador
            paintFrog(g2d);//se pinta el movimiento en pantalla
            moveCar(g2d);//se mueven los carros
            checkMazeC();//se fija que no haya terminado el juego
            checkGame(g2d);//se fija si el jugador gano
        }
    }
    private void moveCar(Graphics2D g2d) {
        //mueve los carros en la matriz
        for (int i = 0; i < N_C; i++) {
            //mueve un carro en x a la velocidad correspondiente
            c_x[i] = c_x[i] + 1;
            c_x[i] = c_x[i] + cSpeed[i];
            if(c_x[i] > 15 * CASILLA)
                c_x[i] = CASILLA;
            drawCar(g2d, c_x[i] + 1, c_y[i] + 1);
            if (frog_x > (c_x[i] - 12) && frog_x < (c_x[i] + 12)
                    && frog_y > (c_y[i] - 12) && frog_y < (c_y[i] + 12)
                    && inGame) {
                //si la rana choca contra un carro muere
                muerto = true;
            }
        }
    }

    private void moveFrog(){
        //se mueve la rana segun la entrada del usuario
        if (req_dx != 0 || req_dy != 0) {
            frog_x += req_dx;
            frog_y += req_dy;
        }
        frog_x = frog_x + (F_SPEED+10) * frog_dx;
        frog_y = frog_y + (F_SPEED+10) * frog_dy;
    }

    private void paintFrog(Graphics2D g2d){
        //pinta la rana
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
        //pinta el carro
        g2d.drawImage(car,x , y, this);
    }
    private void deathC() {
        //se quita una vida
        livesC--;

        if (livesC == 0) {
            cross_ingame = false;
        }//si tiene 0 termina el juego

        continueCrossy();
    }
    private void checkMazeC() {
        int i = 0;
        boolean finished = true;

        while (i < N_CASILLAS * N_CASILLAS && finished) {

            if ((screen_data[i]) != 0) {
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

            continueCrossy();
        }
    }
    private void initCrossy() {
        //inicia el juego, dando vida y el numero de carros
        livesC = 3;
        continueCrossy();
        N_C = 6;
        currentSpeed = 3;
    }
    private void continueCrossy() {
        //se setean las posciones iniciales
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

        frog_x = 8 * CASILLA;  //posicion inicial
        frog_y = 14 * CASILLA;
        frog_dx = 0;//se resetean las direcciones
        frog_dy = 0;
        req_dx = 0;
        req_dy = 0;
        muertoC = false;
        continua();
    }
    private void playPacman(Graphics2D g2d){
        //juega pacman
        if (muerto) {
            //si esta muerto se quita una vida
            deathP();

        } else {
            movePacman();//mueve el jugador
            drawPacman(g2d);//se dibuja el pacman
            moveGhosts(g2d);//mueven los fantasmas
            checkMaze();//se revisa el tablero
            checkWin(g2d);//se revisa si el jugador gana o no
        }
    }

    private void showIntroScreen(Graphics2D g2d) {
        //para poner un mensaje de inicio en el juego
        String start = "Presione ESPACIO para empezar!";
        g2d.setColor(Color.yellow);
        g2d.drawString(start, (SCREEN)/4 - 30, 150);
    }

    private void checkWin(Graphics2D g2d){
        String win = "Has ganado!";
        //si el pacman recoge todos los puntos gana
        if(score == 142){
            g2d.setColor(Color.yellow);
            g2d.drawString(win, SCREEN/4, 180);
            inGame = false;
        }
    }

    private void drawScore(Graphics2D g) {
        //para dibujar la puntuacion
        g.setColor(new Color(5, 181, 79));
        String s = "Score: " + score;
        g.drawString(s, SCREEN / 2 - 35 , SCREEN + 16);
    }

    private void checkMaze() {

        int i = 0;
        boolean finished = true;

        while (i < N_CASILLAS * N_CASILLAS && finished) {

            if ((screen_data[i]) != 0) {
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

            continuePacman();
        }
    }

    private void deathP() {
    //si el pacman muero se le quita una vida y si tiene 0 pierde
        livesP--;

        if (livesP == 0) {
            inGame = false;
        }

        continuePacman();
    }

    private void moveGhosts(Graphics2D g2d) {
        int pos;
        int count;
        //mueven los fantasmas, se chequea que ningun fantasma traspase muros
        for (int i = 0; i < N_G; i++) {
            if (g_x[i] % CASILLA == 0 && g_y[i] % CASILLA == 0) {
                pos = g_x[i] / CASILLA + N_CASILLAS * (int) (g_y[i] / CASILLA);

                count = 0;

                if ((screen_data[pos] & 1) == 0 && g_dx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }

                if ((screen_data[pos] & 2) == 0 && g_dy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }

                if ((screen_data[pos] & 4) == 0 && g_dx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }

                if ((screen_data[pos] & 8) == 0 && g_dy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                if (count == 0) {

                    if ((screen_data[pos] & 15) == 15) {
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
        //se dibuja un fantasma
        g2d.drawImage(ghost, x, y, this);
    }

    private void movePacman() {
        //mueve el jugador con la entrada del usuario
        int pos;
        int ch;

        if (pacman_x % CASILLA == 0 && pacman_y % CASILLA == 0) {
            pos = pacman_x / CASILLA + N_CASILLAS * (int) (pacman_y / CASILLA);
            ch = screen_data[pos];
            if ((ch & 16) != 0) {
                screen_data[pos] = (short) (ch & 15);
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
        //dibuja al pacman
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
    private void initFrame(){
        //inicia el juego
        inGame = true;
    }

    private void initPacman(){
        //inicia pacman con sus vidas el puntaje, el numero de fantasmas y mas
        livesP = 3;
        score = 0;
        continuePacman();
        N_G = 6;
        currentSpeed = 3;
    }
    private void drawMaze(Graphics2D g2d) {
        //dibuja un tablero en pantalla dependiendo del juego en que se encuentre
        short i = 0;
        int x, y;
        if (fase.equals("INICIO")) {
            String welcome = "CONSOLA VIDEOJUEGOS JBONAVAR";
            String C = "1. CROSSY ROAD";
            String P = "2. PAC-MAN";
            String exit = "3. EXIT";
            g2d.setColor(Color.yellow);
            g2d.drawString(welcome,60,85);
            g2d.drawString(C,60,105);
            g2d.drawString(P,60,125);
            g2d.drawString(exit,60,145);
        }else if(fase.equals("CROSSY")){
            for (y = 0; y < SCREEN; y += CASILLA) {
                for (x = 0; x < SCREEN; x += CASILLA) {
                    g2d.setColor(new Color(0,100,0));
                    g2d.setStroke(new BasicStroke(5));

                    if ((screen_data[i] == 2) || screen_data[i] == 1) {
                        g2d.fillRect(x, y, CASILLA, CASILLA);
                    }
                    if ((screen_data[i] == 3)) {
                        g2d.setColor(new Color(255,255,255));
                        g2d.drawLine(x, y, x+8 , y );
                    }
                    i++;
                }
            }
        }else if(fase.equals("PACMAN")){
            for (y = 0; y < SCREEN; y += CASILLA) {
                for (x = 0; x < SCREEN; x += CASILLA) {

                    g2d.setColor(new Color(0,72,251));
                    g2d.setStroke(new BasicStroke(5));

                    if ((screen_data[i] == 0)) {
                        g2d.fillRect(x, y, CASILLA, CASILLA);
                    }

                    if ((screen_data[i] & 1) != 0) {
                        g2d.drawLine(x, y, x, y + CASILLA - 1);
                    }

                    if ((screen_data[i] & 2) != 0) {
                        g2d.drawLine(x, y, x + CASILLA - 1, y);
                    }

                    if ((screen_data[i] & 4) != 0) {
                        g2d.drawLine(x + CASILLA - 1, y, x + CASILLA - 1,
                                y + CASILLA - 1);
                    }

                    if ((screen_data[i] & 8) != 0) {
                        g2d.drawLine(x, y + CASILLA - 1, x + CASILLA - 1,
                                y + CASILLA - 1);
                    }

                    if ((screen_data[i] & 16) != 0) {
                        g2d.setColor(new Color(255,255,255));
                        g2d.fillOval(x + 10, y + 10, 6, 6);
                    }

                    i++;
                }
            }
        }

    }

    private void continuePacman(){
        //setea valores como las casillas iniciales y velocidad de los fantasmaas
        //tambien la posicion inicial y velocidad de pacman
        int dx = 1;
        int random;

        for (int i = 0; i < N_G; i++) {

            g_y[i] = 9 * CASILLA; //posicion inicial
            g_x[i] = 7 * CASILLA;
            g_dy[i] = 0;
            g_dx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (currentSpeed + 1));

            if (random > currentSpeed) {
                random = currentSpeed;
            }

            gSpeed[i] = validSpeeds[random];
        }

        pacman_x = 7 * CASILLA;  //posicion inicial
        pacman_y = 11 * CASILLA;
        pacmand_x = 0;
        pacmand_y = 0;
        req_dx = 0;		//se resetean las posiciones
        req_dy = 0;
        muerto = false;
        continua();
    }

    private void continua(){
        System.out.println("FUNCIONA");
    }
    public void paintComponent(Graphics g) {
        //se utiliza para pintar en pantalla los bytes
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;//se cre el grafico
        g2d.setColor(Color.black);
        g2d.fillRect(0, 0, d.width, d.height);//se pone en negro

        drawMaze(g2d);//se dibuja el tablero que se necesite
        drawScore(g2d);//se dibuja la puntuacion del usuario
        if(inGame){//si inicio el juego
            if(fase.equals("PACMAN")){//si la fase del juego es pacman
                if(pac_ingame){
                    playPacman(g2d);//se juega si ya esta en juego el pacman
                }else{
                    showIntroScreen(g2d);//se le pide que de enter
                }
            }
            else if(fase.equals("CROSSY")){
                if(cross_ingame){
                    playCrossy(g2d);
                }else{
                    showIntroScreen(g2d);
                }
            }
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }


    @Override
    public void run() {
        try {
            ServerSocket server = new ServerSocket(9990);//se abre un server en 9990

            Socket new_tablero = server.accept();//se acepta la entrada

            DataInputStream input = new DataInputStream(new_tablero.getInputStream());

            String b = input.readUTF();//se guarda la entrada

            if (b.equals("EXIT")) {
                System.exit(0);//si el usuario queria salir, sale
            }

            if (b.equals("PAC")) {//si quiere jugar pac man, se empiza el juego
                fase = "PACMAN";
                screen_data = pac_data;
                cross_ingame = false;
                initPacman();
            }

            if (b.equals("CROSS")) {//si quiere jugar crossy road, se empiza el juego
                fase = "CROSSY";
                screen_data = crossy_data;
                pac_ingame = false;
                initCrossy();
            }

            input.close();

            updateUI();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ServerSocket server2 = new ServerSocket(2000);//por este puerto se recibe la jugada del jugador

            Socket res = server2.accept();

            DataInputStream input = new DataInputStream(res.getInputStream());

            String ans = input.readUTF();
            if(ans.equals("INICIO")){//da entes
                fase = "INICIO";
            }
            else if (ans.equals("UP") && pac_ingame) {//presiono hacia arriba
                req_dx = 0;
                req_dy = -1;
                continuePacman();
            } else if (ans.equals("DOWN")&& pac_ingame) {//presiono hacia abajo
                req_dx = 0;
                req_dy = 1;
                continuePacman();
            } else if (ans.equals("LEFT")&& pac_ingame) {//presiono hacia la izquierda
                req_dx = -1;
                req_dy = 0;
                continuePacman();
            } else if (ans.equals("RIGHT")&& pac_ingame){//presiono abajo
                req_dx = 1;
                req_dy = 0;
                continuePacman();
            }else if (ans.equals("UP") && cross_ingame) {
                req_dx = 0;
                req_dy = -1;
                continueCrossy();
            } else if (ans.equals("DOWN")&& cross_ingame) {
                req_dx = 0;
                req_dy = 1;
                continueCrossy();
            } else if (ans.equals("LEFT")&& cross_ingame) {
                req_dx = -1;
                req_dy = 0;
                continueCrossy();
            } else if (ans.equals("RIGHT")&& cross_ingame) {
                req_dx = 1;
                req_dy = 0;
                continueCrossy();
            }else if (ans.equals("INICIAR") && fase.equals("CROSSY")){
                cross_ingame = true;
                initCrossy();
            }else if (ans.equals("INICIAR") && fase.equals("PACMAN")){
                pac_ingame = true;
                initPacman();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
