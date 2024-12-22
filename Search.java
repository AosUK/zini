import java.util.*;

public class Search {

    public static void bfs(int[][] gameplayGrid, int[][] mineGrid, int[][] numericalGrid, List<String> bestActions) {
        Queue<State> queue = new LinkedList<>();
        Map<String, Integer> visitedStates = new HashMap<>();

        queue.add(new State(gameplayGrid, new ArrayList<>()));
        visitedStates.put(zini.stateToString(gameplayGrid), 0);

        while (!queue.isEmpty()) {
            State currentState = queue.poll();
            int[][] currentGrid = currentState.grid;
            List<String> currentActions = currentState.actions;

            if (zini.isComplete(currentGrid, mineGrid)) {
                bestActions.clear();
                bestActions.addAll(currentActions);
                return;
            }

            String currentStateString = zini.stateToString(currentGrid);
            if (visitedStates.containsKey(currentStateString) &&
                visitedStates.get(currentStateString) < currentActions.size()) {
                continue; 
            }
            visitedStates.put(currentStateString, currentActions.size());
            for (int y = 0; y < gameplayGrid.length; y++) {
                for (int x = 0; x < gameplayGrid[0].length; x++) {
 
                    addToQueue(queue, currentGrid, currentActions, visitedStates);

                    int[][] gridBackup = zini.deepCopy(currentGrid);

                    if (currentGrid[y][x] == 0 && mineGrid[y][x] == 1) {
                        zini.Rclick(y, x, currentGrid);
                        List<String> newActions = new ArrayList<>(currentActions);
                        newActions.add("R(" + x + "," + y + ")");
                        addToQueue(queue, currentGrid, newActions, visitedStates);
                        currentGrid = gridBackup; 
                    }


                    if (currentGrid[y][x] == 0 || (currentGrid[y][x] == 1 && numericalGrid[y][x] != 0)) {
                        zini.click(y, x, currentGrid, numericalGrid);
                        List<String> newActions = new ArrayList<>(currentActions);
                        newActions.add("L(" + x + "," + y + ")");
                        addToQueue(queue, currentGrid, newActions, visitedStates);
                        currentGrid = gridBackup; 
                    }
                }
            }
        }
    }

    private static void addToQueue(Queue<State> queue, int[][] grid, List<String> actions, Map<String, Integer> visitedStates) {
        String state = zini.stateToString(grid);
        if (!visitedStates.containsKey(state) || visitedStates.get(state) > actions.size()) {
            visitedStates.put(state, actions.size());
            queue.add(new State(zini.deepCopy(grid), new ArrayList<>(actions)));
        }
    }



    static class State {
        int[][] grid;
        List<String> actions;

        State(int[][] grid, List<String> actions) {
            this.grid = grid;
            this.actions = actions;
        }
    }

    public static void Rclick(int y, int x, int[][] gameplayGrid) {
        gameplayGrid[y][x] = 2;
    }
}
