import java.awt.*;

public class Bullet {
    // Vars Bullet Postions
    private int x, y;
    private double angle;
    private static final int BULLET_SPEED = 10;
    static final int BULLET_RADIUS = 10;

    // Constructer
    public Bullet(int playerX, int playerY, double playerAngle) {
        this.x = playerX;
        this.y = playerY;
        this.angle = playerAngle;
    }

    public void move() {
        // Utilized from youtube
        int deltaX = (int) (BULLET_SPEED * Math.cos(Math.toRadians(angle)));
        int deltaY = (int) (BULLET_SPEED * Math.sin(Math.toRadians(angle)));

        // Update new Postion
        x += deltaX;
        y += deltaY;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x, y, BULLET_RADIUS, BULLET_RADIUS);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, BULLET_RADIUS, BULLET_RADIUS);
    }
}
