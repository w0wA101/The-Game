package m18;

import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main extends Application {

    public static final int TILE_SIZE = 40;
    public static final int MAZE_COLS = 20;
    public static final int MAZE_ROWS = 15;
    public static final int WIDTH = TILE_SIZE * MAZE_COLS;
    public static final int HEIGHT = TILE_SIZE * MAZE_ROWS;
    public static final int TARGET_SCORE = 20;
    public static final int TIME_LIMIT = 60; // seconds

    private Maze maze;
    private Player player;
    private Barista barista;
    private List<Cup> cups;
    private long startTime;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private int score = 0;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize the maze with a more randomized layout
        maze = new Maze(MAZE_ROWS, MAZE_COLS);
        maze.generateSampleMaze();

        // Create the player (starting at cell [1,1]) and the barista (starting at the opposite corner)
        player = new Player(1 * TILE_SIZE + TILE_SIZE/2, 1 * TILE_SIZE + TILE_SIZE/2, TILE_SIZE/2.0);
        barista = new Barista((MAZE_COLS-2)*TILE_SIZE + TILE_SIZE/2, (MAZE_ROWS-2)*TILE_SIZE + TILE_SIZE/2, TILE_SIZE/2.0);

        // Create cups and distribute them over the maze floor (fewer cups than before)
        cups = new ArrayList<>();
        for (int row = 0; row < MAZE_ROWS; row++) {
            for (int col = 0; col < MAZE_COLS; col++) {
                if (maze.getCell(row, col) == Maze.FLOOR && !(row == 1 && col == 1) && !(row == MAZE_ROWS-2 && col == MAZE_COLS-2)) {
                    double rand = Math.random();
                    double x = col * TILE_SIZE + TILE_SIZE/2;
                    double y = row * TILE_SIZE + TILE_SIZE/2;
                    if (rand < 0.03) {
                        cups.add(new LargeCup(x, y, 10)); // Large cup gives 5 points
                    } else if (rand < 0.05) {
                        cups.add(new MediumCup(x, y, 8)); // Medium cup gives 3 points
                    } else if (rand < 0.07) {
                        cups.add(new SmallCup(x, y, 6)); // Small cup gives 1 point
                    }
                }
            }
        }

        // Set up JavaFX scene and canvas
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        StackPane root = new StackPane();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);

        // Handle keyboard input for the player
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.UP) {
                player.setDirection(0, -1);
            } else if (e.getCode() == KeyCode.DOWN) {
                player.setDirection(0, 1);
            } else if (e.getCode() == KeyCode.LEFT) {
                player.setDirection(-1, 0);
            } else if (e.getCode() == KeyCode.RIGHT) {
                player.setDirection(1, 0);
            }
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.UP || e.getCode() == KeyCode.DOWN ||
                    e.getCode() == KeyCode.LEFT || e.getCode() == KeyCode.RIGHT) {
                player.setDirection(0, 0);
            }
        });

        primaryStage.setTitle("M18 - The Cafe Heist");
        primaryStage.setScene(scene);
        primaryStage.show();

        startTime = System.currentTimeMillis();

        // Game loop
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!gameOver) {
                    update();
                    render(gc);
                    checkGameStatus();
                } else {
                    render(gc);
                    stop();
                }
            }
        };
        timer.start();
    }

    // Update positions and handle collisions
    private void update() {
        double delta = 0.016; // assuming ~60 fps

        // Update player movement
        player.update(delta, maze);

        // Barista chases the player with more directed movement
        barista.chase(player, delta, maze);

        // Check collision between player and cups; add cup points and remove cup
        Iterator<Cup> iter = cups.iterator();
        while (iter.hasNext()) {
            Cup cup = iter.next();
            if (player.collidesWith(cup)) {
                score += cup.getPoints();
                iter.remove();
            }
        }

        // The barista also picks up cups, reducing what’s available
        iter = cups.iterator();
        while (iter.hasNext()) {
            Cup cup = iter.next();
            if (barista.collidesWith(cup)) {
                iter.remove();
            }
        }

        // If the barista catches the player, the game is over
        if (player.collidesWith(barista)) {
            gameOver = true;
        }
    }

    // Render the maze, cups, entities, score, and timer
    private void render(GraphicsContext gc) {
        // Clear the canvas
        gc.setFill(Color.BEIGE);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        // Draw maze
        maze.draw(gc, TILE_SIZE);

        // Draw cups
        for (Cup cup : cups) {
            cup.draw(gc);
        }

        // Draw player and barista using images
        player.draw(gc);
        barista.draw(gc);

        // Display score and remaining time
        gc.setFill(Color.BLACK);
        gc.fillText("Score: " + score, 10, 20);

        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        long timeLeft = TIME_LIMIT - elapsed;
        gc.fillText("Time left: " + timeLeft, WIDTH - 100, 20);

        // Display game over message if applicable
        if (gameOver) {
            String message = gameWon ? "You Win!" : "Game Over!";
            gc.setFill(Color.RED);
            gc.fillText(message, WIDTH/2 - 30, HEIGHT/2);
        }
    }

    // Check if the player has won or if time has run out
    private void checkGameStatus() {
        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
        if (score >= TARGET_SCORE) {
            gameOver = true;
            gameWon = true;
        }
        if (elapsed >= TIME_LIMIT) {
            gameOver = true;
        }
    }
}
