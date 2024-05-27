
//Importss
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//PanelGame Class
public class PanelGame extends JPanel {

    // Vars
    private Player player;
    private List<Bullet> bullets = new ArrayList<>();
    private List<Opponents> opponents = new ArrayList<>();
    private BufferedImage background;
    private boolean spaceKeyPressed = false;
    private int totalScore = 0;
    private JLabel scoreLabel;
    private boolean gameOver = false;
    private int highScore = 0;
    private boolean paused = false;

    // Constructer
    public PanelGame() {
        // intialize player
        player = new Player(Player.WIDTH / 2, Player.HEIGHT / 2, "images/rocket.png");

        try {
            background = ImageIO.read(new File("images/spacewallpaper.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // reate and add a score label at the top middle of the frame
        scoreLabel = new JLabel("Total Score: " + totalScore);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);
        scoreLabel.setFont(new Font("sans serif", Font.BOLD, 40));

        // Add
        setLayout(new BorderLayout());
        add(scoreLabel, BorderLayout.NORTH);
        addKeyListener(new KeyHandler(this));
        setFocusable(true);
        requestFocus();
        addKeyListener(new PauseKeyListener());
        addKeyListener(new KeyHandler(this));
        setFocusable(true);
        requestFocus();

        // Add TImer
        Timer timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!paused) {
                    update();
                    repaint();
                }
            }
        });

        timer.start();

        InputMap inputMap = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        // Key listners
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "rotateLeft");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "rotateRight");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "shoot");

        // Movments

        actionMap.put("moveUp", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.move();
            }
        });
        // Rotations of ROcketship
        actionMap.put("rotateLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.rotate(-12);
            }
        });
        actionMap.put("rotateRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.rotate(12);
            }
        });

        // Call shoot wheenver space arrow Key
        actionMap.put("shoot", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shoot();
            }
        });

        initializeOpponents();
    }

    // update Functions
    private void update() {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();
            bullet.move();
            if (bullet.getX() < 0 || bullet.getX() > Player.WIDTH || bullet.getY() < 0
                    || bullet.getY() > Player.HEIGHT) {
                bulletIterator.remove();
            }
        }

        Iterator<Opponents> opponentIterator = opponents.iterator();
        while (opponentIterator.hasNext()) {
            Opponents opponent = opponentIterator.next();
            opponent.move();
        }
        // Check for bulletasteroid collisions
        checkBulletAsteroidCollisions();

        opponentIterator = opponents.iterator();
        while (opponentIterator.hasNext()) {
            Opponents opponent = opponentIterator.next();
            if (opponent.getHitCount() >= 2) {
                opponentIterator.remove();
            }
        }

        initializeOpponents();
    }

    // Check method to see if any collisoin is made with a bullet
    private void checkBulletAsteroidCollisions() {
        Iterator<Bullet> bulletIterator = bullets.iterator();
        while (bulletIterator.hasNext()) {
            Bullet bullet = bulletIterator.next();

            Iterator<Opponents> opponentIterator = opponents.iterator();
            while (opponentIterator.hasNext()) {
                Opponents opponent = opponentIterator.next();

                if (collision(bullet, opponent)) {
                    bulletIterator.remove();
                    opponent.increaseHitCount();
                    System.out.println("Asteroid hit by bullet");

                    if (!opponent.isHitBefore()) {
                        // increase score
                        // change hit value
                        totalScore++;
                        opponent.setHitBefore(true);
                    }

                    initializeOpponents();
                }
            }
        }

        // If rocket makes collision with any asteroid GAME OVER
        if (collisionRocket()) {
            gameOver = true;
            paused = true;
            checkAndUpdateHighScore();
        }
        // Update Score method
        updateScoreLabel();
    }

    // Method to display total Score
    private void updateScoreLabel() {
        if (gameOver) {
            scoreLabel.setText("GAME OVER. High Score: " + highScore);
            scoreLabel.setForeground(Color.RED);
        } else {
            scoreLabel.setText("Total Score: " + totalScore);
            scoreLabel.setForeground(Color.WHITE);
        }
    }

    // Wont utilize for Submission
    private void checkAndUpdateHighScore() {
        if (totalScore > highScore) {
            highScore = totalScore;
        }
    }

    // Detect Rocket COllision
    private boolean collisionRocket() {
        Rectangle rocketBounds = new Rectangle(player.getX(), player.getY(),
                player.getPlayerImage().getWidth(), player.getPlayerImage().getHeight());

        for (Opponents opponent : opponents) {
            Rectangle opponentBounds = new Rectangle((int) opponent.getX(), (int) opponent.getY(),
                    (int) Opponents.ASTEROID_SIZE, (int) Opponents.ASTEROID_SIZE);

            if (rocketBounds.intersects(opponentBounds)) {
                return true;
            }
        }

        return false;
    }

    // Bullet collsision with asteroid
    private boolean collision(Bullet bullet, Opponents opponent) {
        Rectangle bulletBounds = bullet.getBounds();
        Rectangle opponentBounds = new Rectangle((int) opponent.getX(), (int) opponent.getY(),
                (int) Opponents.ASTEROID_SIZE, (int) Opponents.ASTEROID_SIZE);

        return bulletBounds.intersects(opponentBounds);
    }

    // Default Asteroid
    private void initializeOpponents() {
        if (opponents.size() < 4) {
            for (int i = 0; i < 4; i++) {
                double randomX = generateRandomPosition(Player.WIDTH);
                double randomY = generateRandomPosition(Player.HEIGHT);
                Opponents opponent = new Opponents(randomX, randomY, 1, Math.random() * 360);

                while (checkOverlap(opponent)) {
                    randomX = generateRandomPosition(Player.WIDTH);
                    randomY = generateRandomPosition(Player.HEIGHT);
                    opponent.setX(randomX);
                    opponent.setY(randomY);
                }
                opponents.add(opponent);
            }
        }
    }

    // Asteroid random postions generation
    private double generateRandomPosition(int max) {
        return Math.random() * max;
    }

    // make sure no asteroids overlap each other
    private boolean checkOverlap(Opponents newOpponent) {
        for (Opponents existingOpponent : opponents) {
            double distance = Math.hypot(newOpponent.getX() - existingOpponent.getX(),
                    newOpponent.getY() - existingOpponent.getY());
            if (distance < Opponents.ASTEROID_SIZE * 2) {
                return true;
            }
        }
        return false;
    }

    // Paint
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        player.draw(g);

        for (Bullet bullet : bullets) {
            bullet.draw(g);
        }

        for (Opponents opponent : opponents) {
            g.drawImage(opponent.getImage(), (int) opponent.getX(), (int) opponent.getY(),
                    (int) Opponents.ASTEROID_SIZE, (int) Opponents.ASTEROID_SIZE, this);
        }
    }

    // Space Shoot bullets
    public boolean isSpaceKeyPressed() {
        return spaceKeyPressed;
    }

    public void setSpaceKeyPressed(boolean spaceKeyPressed) {
        this.spaceKeyPressed = spaceKeyPressed;
    }

    // Detect space
    public void shoot() {
        if (isSpaceKeyPressed()) {
            Bullet bullet = new Bullet(player.getX() + player.getPlayerImage().getWidth() / 2,
                    player.getY() + player.getPlayerImage().getHeight() / 2, player.getPlayerAngle());
            bullets.add(bullet);
        }
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    // Whenever gameOver pause game state
    private class PauseKeyListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            if (keyCode == KeyEvent.VK_P) {
                paused = !paused;
                if (paused && gameOver) {
                    // unUtilized
                    resetGame();
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        private void resetGame() {
            // set to 0
            bullets.clear();
            opponents.clear();
            totalScore = 0;
            highScore = 0;
            gameOver = false;
            paused = false;
            initializeOpponents();
            updateScoreLabel();
        }
    }

}