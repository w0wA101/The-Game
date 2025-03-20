package m18;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class LargeCup extends Cup {
    private Image cupImage;

    public LargeCup(double x, double y, double radius) {
        super(x, y, radius);
        // Load large cup image (ensure /images/large_cup.png exists in resources)
        cupImage = new Image(getClass().getResourceAsStream("/images/large_cup.png"));
    }
    @Override
    public int getPoints() {
        return 5;
    }
    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(cupImage, x - radius, y - radius, radius * 2, radius * 2);
    }
}
