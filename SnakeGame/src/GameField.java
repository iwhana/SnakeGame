import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 495;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 480;
    private Image dot;
    private Image apple;
    private Image appleEvil;
    private int appleX;
    private int appleY;
    private int appleX2;
    private int appleY2;
    private int appleX3;
    private int appleY3;
    private int[] x = new int[ALL_DOTS];
    private int[] y = new int[ALL_DOTS];
    private int dots;
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    private boolean inGame = false;
    private int speed = 200;
    private int score;
    boolean isFailed = false;
    Timer timer = new Timer(speed, this);
    private int bestScore;

    public GameField() {
        setBackground(Color.white);
        loadImages();
        addKeyListener(new FieldKey());
        setFocusable(true);
        initGame();
    }

    public void loadImages() {
        ImageIcon iia = new ImageIcon("apple.png");
        apple = iia.getImage();
        ImageIcon iiae = new ImageIcon("appleEvil.png");
        appleEvil = iiae.getImage();
        ImageIcon iid = new ImageIcon("dot.png");
        dot = iid.getImage();
    }

    public void initGame() {
        score = 0;
        inGame = true;
        isFailed = false;
        right = true;
        up = false;
        down = false;
        left = false;
        dots = 3;
        speed = 200;
        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i * DOT_SIZE;
            y[i] = 48;
        }
        timer.stop();
        timer = new Timer(speed, this);
        timer.start();
        createApple();
        createApple2();
        createApple3();
    }

    public void createApple() {
        appleX = new Random().nextInt(31) * DOT_SIZE;
        appleY = new Random().nextInt(31) * DOT_SIZE;
    }

    public void createApple2() {
        appleX2 = new Random().nextInt(31) * DOT_SIZE;
        appleY2 = new Random().nextInt(31) * DOT_SIZE;
    }

    public void createApple3() {
        appleX3 = new Random().nextInt(31) * DOT_SIZE;
        appleY3 = new Random().nextInt(31) * DOT_SIZE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame) {
            g.drawImage(apple, appleX, appleY, this);
            g.drawImage(apple, appleX2, appleY2, this);
            g.drawImage(appleEvil, appleX3, appleY3, this);
            String str = "Score: " + score;
            g.setColor(Color.black);
            g.drawString(str, 420, 20);
            for (int i = 0; i < dots; i++)
                g.drawImage(dot, x[i], y[i], this);
        }
        if (!inGame) {
            g.setColor(Color.black);
            g.drawString("Press Space to start / pause", 170, 230);
        }
        if (isFailed) {
            if (score > bestScore)
                bestScore = score;
            g.setColor(Color.black);
            g.drawString("Game Over!", 170, 250);
            g.drawString("Score: " + score, 170, 270);
            g.drawString("Best Score: " + bestScore, 170, 290);
        }

    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (left)
            x[0] -= DOT_SIZE;
        if (right)
            x[0] += DOT_SIZE;
        if (up)
            y[0] -= DOT_SIZE;
        if (down)
            y[0] += DOT_SIZE;
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            score++;
            if (speed > 60) {
                speed = speed - 17;
                timer.stop();
                timer = new Timer(speed, this);
                timer.start();
            }
            createApple();
            createApple3();
        }
        if (x[0] == appleX2 && y[0] == appleY2) {
            dots++;
            score++;
            if (speed > 60) {
                speed = speed - 17;
                timer.stop();
                timer = new Timer(speed, this);
                timer.start();
            }
            createApple2();
            createApple3();
        }
        if (x[0] == appleX3 && y[0] == appleY3)
            isFailed = true;
    }

    public void checkCollisions() {
        for (int i = dots; i > 0; i--) {
            if (i > 4 && x[0] == x[i] && y[0] == y[i]) {
                isFailed = true;
                inGame = false;
            }
        }
        if (x[0] > SIZE) {
            isFailed = true;
            inGame = false;
        }
        if (x[0] < 0) {
            isFailed = true;
            inGame = false;
        }
        if (y[0] > SIZE){
            isFailed = true;
        inGame = false;
        }
        if(y[0] < 0){
            isFailed =true;
            inGame =false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(inGame && !isFailed) {
            checkApple();
            checkCollisions();
            move();
        }
        repaint();
    }

    class FieldKey extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_SPACE) {
                if (isFailed)
                    initGame();
                else
                    inGame = !inGame;
            }
            if(key == KeyEvent.VK_RIGHT && !left)
            {
                right = true;
                up = false;
                down = false;
            }
            else if(key == KeyEvent.VK_UP && !down)
            {
                left = false;
                up = true;
                right = false;
            }
            else if(key == KeyEvent.VK_LEFT && !right)
            {
                left = true;
                up = false;
                down = false;
            }
            else if(key == KeyEvent.VK_DOWN && !up)
            {
                left = false;
                right = false;
                down = true;
            }
        }
    }
}
