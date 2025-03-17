package m18;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class SmallCup extends Cup {
    private Image cupImage;

    public SmallCup(double x, double y, double radius) {
        super(x, y, radius);
        // Load small cup image (ensure /images/small_cup.png exists in your resources)
        cupImage = new Image(getClass().getResourceAsStream("/images/small_cup.png"));
    }
    @Override
    public int getPoints() {
        return 1;
    }
    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(cupImage, x - radius, y - radius, radius * 2, radius * 2);
    }
}
