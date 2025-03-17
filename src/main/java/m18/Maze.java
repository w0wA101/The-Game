package m18;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Maze {
    public static final int WALL = 0;
    public static final int FLOOR = 1;

    private int rows;
    private int cols;
    private int[][] grid;

    public Maze(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        grid = new int[rows][cols];
    }

    // Generates a maze with boundaries, a central horizontal wall (with a gap), and random obstacles
    public void generateSampleMaze() {
        // Create boundaries
        for (int i = 0; i < rows; i++) {
            grid[i][0] = WALL;
            grid[i][cols - 1] = WALL;
        }
        for (int j = 0; j < cols; j++) {
            grid[0][j] = WALL;
            grid[rows - 1][j] = WALL;
        }
        // Fill inner cells with floor
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                grid[i][j] = FLOOR;
            }
        }

        // Add a horizontal wall in the middle with a gap
        int midRow = rows / 2;
        for (int j = 1; j < cols - 1; j++) {
            grid[midRow][j] = WALL;
        }
        // Create a gap in the horizontal wall
        grid[midRow][cols / 2] = FLOOR;

        // Add extra random obstacles to enhance maze randomization (except critical cells)
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                // Avoid overwriting player's spawn (1,1), barista spawn (rows-2, cols-2), and the gap in the wall
                if (!((i == 1 && j == 1) || (i == rows - 2 && j == cols - 2) || (i == midRow && j == cols/2))) {
                    if (Math.random() < 0.15) { // 15% chance to place a wall
                        grid[i][j] = WALL;
                    }
                }
            }
        }
    }

    public int getCell(int row, int col) {
        return grid[row][col];
    }

    public void draw(GraphicsContext gc, int tileSize) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == WALL) {
                    gc.setFill(Color.DARKSLATEGRAY);
                    gc.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                } else {
                    gc.setFill(Color.LIGHTGRAY);
                    gc.fillRect(j * tileSize, i * tileSize, tileSize, tileSize);
                }
            }
        }
    }

    // Check if a given point (in pixels) is walkable (i.e., is on a floor cell)
    public boolean isWalkable(double x, double y, int tileSize) {
        int col = (int)(x / tileSize);
        int row = (int)(y / tileSize);
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            return grid[row][col] == FLOOR;
        }
        return false;
    }
}
