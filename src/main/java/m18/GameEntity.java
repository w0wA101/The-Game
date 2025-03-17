package m18;

public abstract class GameEntity {
    protected double x, y;
    protected double radius;

    public GameEntity(double x, double y, double radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Simple circular collision detection
    public boolean collidesWith(GameEntity other) {
        double dx = x - other.x;
        double dy = y - other.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < this.radius + other.radius;
    }

    public abstract void draw(javafx.scene.canvas.GraphicsContext gc);
}
