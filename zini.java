public class zini {
    public static void main(String[] args) {
        String input = "3/3/000010000";
        int[][] mineGrid = generateMineGrid(input);
        int[][] numericalGrid = generateNumericalGrid(mineGrid);
        int rows = mineGrid.length;
        int cols = mineGrid[0].length;
        int[][] gameplayGrid = new int[rows][cols];
        closeGameplayGrid(gameplayGrid);

        printGrid(gameplayGrid);
        System.out.println("-");
        click(0, 0, gameplayGrid, numericalGrid);
        printGrid(gameplayGrid);
        System.out.println("-");
        rightClick(1, 1, gameplayGrid);
        printGrid(gameplayGrid);
        System.out.println("-");
        click(0, 0, gameplayGrid, numericalGrid);
        printGrid(gameplayGrid);
    }

    public static void click(int y, int x, int[][] gameplayGrid, int[][] numericalGrid) {
        openCell(y, x, gameplayGrid, numericalGrid);
        if (numericalGrid[y][x] != 0) {
            checkAndOpenSurrounding(y, x, gameplayGrid, numericalGrid);
        }
    }
    

    public static void rightClick(int y, int x, int[][] gameplayGrid) {
        if (gameplayGrid[y][x] == 0) {
            gameplayGrid[y][x] = 2;
        }
    }

    public static void checkAndOpenSurrounding(int y, int x, int[][] gameplayGrid, int[][] numericalGrid) {
        int flagCount = 0;
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                if (dy == 0 && dx == 0) continue;
                int ny = y + dy, nx = x + dx;
                if (ny >= 0 && ny < gameplayGrid.length && nx >= 0 && nx < gameplayGrid[0].length) {
                    if (gameplayGrid[ny][nx] == 2) flagCount++;
                }
            }
        }
        if (flagCount == numericalGrid[y][x]) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dx = -1; dx <= 1; dx++) {
                    if (dy == 0 && dx == 0) continue;
                    int ny = y + dy, nx = x + dx;
                    if (ny >= 0 && ny < gameplayGrid.length && nx >= 0 && nx < gameplayGrid[0].length) {
                        if (gameplayGrid[ny][nx] == 0 && numericalGrid[ny][nx] != -1) {
                            openCell(ny, nx, gameplayGrid, numericalGrid);
                        }
                    }
                }
            }
        }
    }

    public static void openCell(int y, int x, int[][] gameplayGrid, int[][] numericalGrid) {
        if (y < 0 || y >= gameplayGrid.length || x < 0 || x >= gameplayGrid[0].length) return;
        if (gameplayGrid[y][x] != 0) return;
        gameplayGrid[y][x] = 1;
        if (numericalGrid[y][x] == 0) {
            for (int dy = -1; dy <= 1; dy++) {
                for (int dx = -1; dx <= 1; dx++) {
                    if (dy == 0 && dx == 0) continue;
                    openCell(y + dy, x + dx, gameplayGrid, numericalGrid);
                }
            }
        }
    }

    public static int[][] generateMineGrid(String input) {
        String[] parts = input.split("/");
        int cols = Integer.parseInt(parts[0]);
        int rows = Integer.parseInt(parts[1]);
        String gridData = parts[2];
        int[][] mineGrid = new int[rows][cols];
        int index = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                mineGrid[y][x] = Character.getNumericValue(gridData.charAt(index));
                index++;
            }
        }
        return mineGrid;
    }

    public static int[][] generateNumericalGrid(int[][] mineGrid) {
        int rows = mineGrid.length;
        int cols = mineGrid[0].length;
        int[][] numericalGrid = new int[rows][cols];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (mineGrid[y][x] == 1) {
                    numericalGrid[y][x] = -1;
                } else {
                    int mineCount = 0;
                    for (int dy = -1; dy <= 1; dy++) {
                        for (int dx = -1; dx <= 1; dx++) {
                            int ny = y + dy;
                            int nx = x + dx;
                            if (ny >= 0 && ny < rows && nx >= 0 && nx < cols && mineGrid[ny][nx] == 1) {
                                mineCount++;
                            }
                        }
                    }
                    numericalGrid[y][x] = mineCount;
                }
            }
        }

        return numericalGrid;
    }

    public static void closeGameplayGrid(int[][] gameplayGrid) {
        for (int y = 0; y < gameplayGrid.length; y++) {
            for (int x = 0; x < gameplayGrid[0].length; x++) {
                gameplayGrid[y][x] = 0;
            }
        }
    }

    public static void printGrid(int[][] grid) {
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
