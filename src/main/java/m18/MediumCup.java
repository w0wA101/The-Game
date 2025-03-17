package m18;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class MediumCup extends Cup {
    private Image cupImage;

    public MediumCup(double x, double y, double radius) {
        super(x, y, radius);
        // Load medium cup image (ensure /images/medium_cup.png exists in your resources)
        cupImage = new Image(getClass().getResourceAsStream("/images/medium_cup.png"));
    }
    @Override
    public int getPoints() {
        return 3;
    }
    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(cupImage, x - radius, y - radius, radius * 2, radius * 2);
    }
}
