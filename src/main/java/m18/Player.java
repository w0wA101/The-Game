package m18;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Player extends GameEntity {
    private double dx = 0, dy = 0;
    private double speed = 100; // pixels per second
    private Image playerImage;

    public Player(double x, double y, double radius) {
        super(x, y, radius);
        // Load player image (ensure /images/player.png exists in resources)
        playerImage = new Image(getClass().getResourceAsStream("/images/player.png"));
    }

    public void setDirection(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update(double delta, Maze maze) {
        double nextX = x + dx * speed * delta;
        double nextY = y + dy * speed * delta;
        // Only move if the next position is walkable
        if (maze.isWalkable(nextX, nextY, Main.TILE_SIZE)) {
            x = nextX;
            y = nextY;
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(playerImage, x - radius, y - radius, radius * 2, radius * 2);
    }
}
