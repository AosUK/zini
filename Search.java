import java.util.*;
public class Search {
    static int maxDepth = 2;

    public static void bfs(int[][] gameplayGrid, int[][] mineGrid, int[][] numericalGrid, List<String> bestActions, int maxDepth) {
        Queue<State> queue = new LinkedList<>();
        Map<String, Integer> visitedStates = new HashMap<>();
    
   
        queue.add(new State(gameplayGrid, new ArrayList<>()));
        visitedStates.put(zini.stateToString(gameplayGrid), 0);
    
        int best3BV = -1; 
        List<String> bestMoves = new ArrayList<>(); 
    
    
        int[][] bv3Grid = zini.generateBv3Grid(gameplayGrid, numericalGrid);
    
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
    
         
            int current3BV = zini.getSolvedBV3(bv3Grid, currentGrid);
    
           
            if (current3BV > best3BV) {
                best3BV = current3BV;
                bestMoves.clear();
                bestMoves.addAll(currentActions); 
            }
    
        
            if (currentActions.size() >= maxDepth) {
                
                if (current3BV > best3BV) {
                    best3BV = current3BV;
                    bestActions.clear();
                    bestActions.addAll(currentActions); 
                }
                continue; 
            }
    
    
            for (int y = 0; y < gameplayGrid.length; y++) {
                for (int x = 0; x < gameplayGrid[0].length; x++) {
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
    

        if (!bestMoves.isEmpty()) {
            bestActions.clear();
            bestActions.addAll(bestMoves);
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
}
