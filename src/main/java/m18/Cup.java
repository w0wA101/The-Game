package m18;

import javafx.scene.canvas.GraphicsContext;

public abstract class Cup extends GameEntity {
    public Cup(double x, double y, double radius) {
        super(x, y, radius);
    }
    public abstract int getPoints();
}
