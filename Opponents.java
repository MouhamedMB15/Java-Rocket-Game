
//Imports
import javax.swing.*;
import java.awt.*;
import java.awt.image.ImageObserver;

public class Opponents {
    // Vars
    public static final double ASTEROID_SIZE = 74;
    private double x;
    private double y;
    private double speed;
    private double angle;
    private final Image image;
    private int hitCount = 0;
    private boolean hitBefore = false;

    // Constructers
    public Opponents(double x, double y, double speed, double angle) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.angle = angle;
        this.image = new ImageIcon("images/purpleasteroid.png").getImage();
    }

    public void move() {
        x += speed * Math.cos(Math.toRadians(angle));
        y += speed * Math.sin(Math.toRadians(angle));

        if (x < 0)
            x = Player.WIDTH;
        if (x > Player.WIDTH)
            x = 0;
        if (y < 0)
            y = Player.HEIGHT;
        if (y > Player.HEIGHT)
            y = 0;
    }

    public Image getImage() {
        return image;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void increaseHitCount() {
        hitCount++;
    }

    public boolean isHitBefore() {
        return hitBefore;
    }

    public void setHitBefore(boolean hitBefore) {
        this.hitBefore = hitBefore;
    }

}
