import java.util.Arrays;
import java.util.List;
import java.io.PrintStream;
import java.util.ArrayList;

public class zini {
    public static int bv3; 

    public static void main(String[] args) {
        String input = "9/9/001000100010010000010010000001001000000010000000001000000000000000000000000000000";
        int[][] mineGrid = generateMineGrid(input);
        int[][] numericalGrid = generateNumericalGrid(mineGrid);
        int rows = mineGrid.length;
        int cols = mineGrid[0].length;
        int[][] gameplayGrid = new int[rows][cols];
        closeGameplayGrid(gameplayGrid);
        int[][] bv3Grid = generateBv3Grid(gameplayGrid, numericalGrid);
        closeGameplayGrid(gameplayGrid);

        List<String> bestActions = new ArrayList<>();

        while (!isComplete(gameplayGrid, bv3Grid)) {
            List<String> currentBestActions = new ArrayList<>();
            Search.bfs(gameplayGrid, mineGrid, numericalGrid,bv3Grid, currentBestActions, Search.maxDepth);

            bestActions.addAll(currentBestActions);


            for (String action : currentBestActions) {
                String[] parts = action.substring(2, action.length() - 1).split(",");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);

                if (action.startsWith("R")) {
                    Rclick(y, x, gameplayGrid); 
                } else if (action.startsWith("L")) {
                    click(y, x, gameplayGrid, numericalGrid); 
                }
            }



        }

        System.out.println(String.join("/", bestActions));
        System.out.println(bestActions.size());
    }

    public static int[][] generateBv3Grid(int[][] gameplayGrid, int[][] numericalGrid) {
        int rows = numericalGrid.length;
        int cols = numericalGrid[0].length;
        int[][] bv3Grid = new int[rows][cols];
        bv3 = 0;
        
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                bv3Grid[y][x] = 0;
            }
        }

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (numericalGrid[y][x] == 0 && gameplayGrid[y][x]==0) {
                    openCell(y, x, gameplayGrid, numericalGrid);
                    bv3Grid[y][x] = 1; 
                    bv3++; 
                }
            }
        }

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (gameplayGrid[y][x] == 0 && numericalGrid[y][x] >0) {
                    openCell(y, x, gameplayGrid, numericalGrid);
                    bv3Grid[y][x] = 1;
                    bv3++;
                }
            }
        }
        return bv3Grid;
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

    public static void printGrid(int[][] grid) {
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    public static int getSolvedBV3(int[][] bv3Grid, int[][] gameplayGrid) {
        int solvedCount = 0;
        int rows = bv3Grid.length;
        int cols = bv3Grid[0].length;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (bv3Grid[y][x] == 1 && gameplayGrid[y][x] == 1) {
                    solvedCount++;
                }
            }
        }
        return solvedCount;
    }

    public static String stateToString(int[][] gameplayGrid) {
        StringBuilder sb = new StringBuilder();
        for (int[] row : gameplayGrid) {
            for (int cell : row) {
                sb.append(cell);
            }
        }
        return sb.toString();
    }

    public static boolean isComplete(int[][] gameplayGrid, int[][] bv3Grid) {
        for (int y = 0; y < gameplayGrid.length; y++) {
            for (int x = 0; x < gameplayGrid[0].length; x++) {
                if (bv3Grid[y][x] == 1 && gameplayGrid[y][x] != 1) {
                    return false;
                }
            }
        }
        return true;
    }
    public static int[][] deepCopy(int[][] original) {
        return Arrays.stream(original).map(int[]::clone).toArray(int[][]::new);
    }
    public static void chord(int y, int x, int[][] gameplayGrid, int[][] numericalGrid) {
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

    public static void click(int y, int x, int[][] gameplayGrid, int[][] numericalGrid) {
        int originalState = gameplayGrid[y][x];
        openCell(y, x, gameplayGrid, numericalGrid);
        if (originalState == 1) {
            chord(y, x, gameplayGrid, numericalGrid);
        }
    }

    public static void Rclick(int y, int x, int[][] gameplayGrid) {
        gameplayGrid[y][x] = 2;
    }
}
