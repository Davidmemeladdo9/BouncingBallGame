import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class BouncingBallGame extends JFrame {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;
    private static final int BALL_SIZE = 20;

    private int paddleX = WIDTH / 2 - PADDLE_WIDTH / 2;
    private int ballX, ballY;
    private int ballSpeedX = 3;
    private int ballSpeedY = 2;
    private ArrayList<Point> balls;
    private int score = 0;
    private boolean gameOver = false;

    private Timer timer;

    public BouncingBallGame() {
        setTitle("Bouncing Ball Game");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        balls = new ArrayList<>();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (!gameOver) {
                    if (key == KeyEvent.VK_LEFT && paddleX > 0) {
                        paddleX -= 20;
                    } else if (key == KeyEvent.VK_RIGHT && paddleX < WIDTH - PADDLE_WIDTH) {
                        paddleX += 20;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        timer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    moveBall();
                    checkCollision();
                    repaint();
                }
            }
        });

        timer.start();

        generateBall();
    }

    private void generateBall() {
        ballX = new Random().nextInt(WIDTH - BALL_SIZE);
        ballY = 0;
        balls.add(new Point(ballX, ballY));
    }

    private void moveBall() {
        for (Point ball : balls) {
            ball.x += ballSpeedX;
            ball.y += ballSpeedY;

            // Bounce off the walls
            if (ball.x <= 0 || ball.x >= WIDTH - BALL_SIZE) {
                ballSpeedX = -ballSpeedX;
            }

            // Bounce off the ceiling
            if (ball.y <= 0) {
                ballSpeedY = -ballSpeedY;
            }

            // Check if the ball reached the bottom without being caught
            if (ball.y >= HEIGHT) {
                gameOver = true;
                timer.stop();
            }
        }
    }

    private void checkCollision() {
        for (Point ball : balls) {
            if (ball.x >= paddleX && ball.x <= paddleX + PADDLE_WIDTH && ball.y >= HEIGHT - PADDLE_HEIGHT - BALL_SIZE) {
                ballSpeedY = -ballSpeedY;  // Bounce the ball when caught
                score++;
                balls.remove(ball);
                generateBall();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw paddle
        g2d.fillRect(paddleX, HEIGHT - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Draw balls
        for (Point ball : balls) {
            g2d.fillOval(ball.x, ball.y, BALL_SIZE, BALL_SIZE);
        }

        // Draw score
        g2d.setColor(Color.BLACK);
        g2d.drawString("Score: " + score, 10, 20);

        // Draw game over message
        if (gameOver) {
            g2d.drawString("Game Over! Your Score: " + score, WIDTH / 2 - 100, HEIGHT / 2);
        }

        Toolkit.getDefaultToolkit().sync();  // Ensure smooth animation
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new BouncingBallGame().setVisible(true);
            }
        });
    }
}
