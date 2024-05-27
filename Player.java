
//Imports
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Player {
    // Vars
    private BufferedImage playerImage;
    private int playerX, playerY;
    private double playerAngle;
    private static final int PLAYER_SPEED = 17;
    public static final int WIDTH = 1900;
    public static final int HEIGHT = 1050;

    // Constructer
    public Player(int x, int y, String imagePath) {
        try {
            playerImage = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        playerX = x - playerImage.getWidth() / 2;
        playerY = y - playerImage.getHeight() / 2;
        playerAngle = 0;
    }

    // Move rocketShip
    public void move() {
        double angleInRadians = Math.toRadians(playerAngle);

        int deltaX = (int) (PLAYER_SPEED * Math.cos(angleInRadians));
        int deltaY = (int) (PLAYER_SPEED * Math.sin(angleInRadians));

        playerX += deltaX;
        playerY += deltaY;

        if (playerX < 0)
            playerX = WIDTH;
        if (playerX > WIDTH)
            playerX = 0;
        if (playerY < 0)
            playerY = HEIGHT;
        if (playerY > HEIGHT)
            playerY = 0;
    }

    // Rotations
    public void rotate(int direction) {
        playerAngle += direction;
        if (playerAngle < 0)
            playerAngle += 360;
        if (playerAngle >= 360)
            playerAngle -= 360;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        AffineTransform at = AffineTransform.getTranslateInstance(playerX, playerY);
        at.rotate(Math.toRadians(playerAngle + 45), playerImage.getWidth() / 2.0, playerImage.getHeight() / 2.0);
        g2d.drawImage(playerImage, at, null);
    }

    // Gettors
    public int getX() {
        return playerX;
    }

    public int getY() {
        return playerY;
    }

    public double getPlayerAngle() {
        return playerAngle;
    }

    public BufferedImage getPlayerImage() {
        return playerImage;
    }
}