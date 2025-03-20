package m18;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Barista extends GameEntity {
    private double speed = 75; // increased speed for a more aggressive chase
    private Image baristaImage;

    public Barista(double x, double y, double radius) {
        super(x, y, radius);
        // Load barista image (ensure /images/barista.png exists in resources)
        baristaImage = new Image(getClass().getResourceAsStream("/images/barista.png"));
    }

    // Chase the player using a more directed approach
    public void chase(Player player, double delta, Maze maze) {
        double diffX = player.getX() - x;
        double diffY = player.getY() - y;
        double distance = Math.sqrt(diffX * diffX + diffY * diffY);
        if (distance != 0) {
            double moveX = (diffX / distance) * speed * delta;
            double moveY = (diffY / distance) * speed * delta;
            double nextX = x + moveX;
            double nextY = y + moveY;
            if (maze.isWalkable(nextX, nextY, Main.TILE_SIZE)) {
                x = nextX;
                y = nextY;
            } else {
                // If diagonal move is blocked, try horizontal then vertical movement
                if (maze.isWalkable(x + moveX, y, Main.TILE_SIZE)) {
                    x += moveX;
                } else if (maze.isWalkable(x, y + moveY, Main.TILE_SIZE)) {
                    y += moveY;
                }
            }
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(baristaImage, x - radius, y - radius, radius * 2, radius * 2);
    }
}
