package sample.solver;

import sample.models.Point;
import sample.models.Puzzle;
import sample.models.State;

import java.util.*;

/**
 * Created by Mateusz Grabarski on 24.05.2018.
 */
public class PuzzleSolver {

    private Map<Puzzle, State> stateByPuzzle;
    private Map<Integer, Point> pieceCorrectPlacement;
    private Puzzle correctPuzzle;
    private Puzzle puzzle;

    public PuzzleSolver(Puzzle correctPuzzle, Puzzle puzzle) {
        this.correctPuzzle = correctPuzzle;
        this.puzzle = puzzle;
        this.stateByPuzzle = new HashMap<>();
        fillPieceCorrectPlacement();
    }

    private void fillPieceCorrectPlacement() {
        pieceCorrectPlacement = new HashMap<>();
        int sideLength = correctPuzzle.getSideLength();
        for (int x = 0; x < sideLength; x++) {
            for (int y = 0; y < sideLength; y++) {
                Point point = new Point(x, y);
                int piece = correctPuzzle.getPieceAt(point);
                pieceCorrectPlacement.put(piece, point);
            }
        }
    }


    public List<Puzzle> solve() {
        State state = new State(puzzle, null, getScoreFnValue(puzzle));
        stateByPuzzle.put(puzzle, state);

        int counter = 0;
        while (state.getScoreFnValue() > 0) {
            takeALookAround(state.getPuzzle());
            state.visit();
            state = findBestUnvisitedState();
            counter++;
        }
        System.out.println(counter);

        List<Puzzle> result = new ArrayList<>();
        while (!state.getPuzzle().equals(puzzle)) {
            result.add(state.getPuzzle());
            state = state.getOrigin();
        }
        result.add(puzzle);
        Collections.reverse(result);

        result.forEach(p -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(p);
        });

        return result;
    }

    private State findBestUnvisitedState() {
        return stateByPuzzle.values()
                .stream()
                .filter(s -> !s.isVisited())
                .min((s1, s2) -> s1.getScoreFnValue() - s2.getScoreFnValue())
                .get();
    }

    private void takeALookAround(Puzzle puzzle) {
        List<Puzzle> surrounding = Arrays.asList(
                puzzle.moveUp(),
                puzzle.moveDown(),
                puzzle.moveLeft(),
                puzzle.moveRight()
        );
        for (Puzzle surroundingPuzzle : surrounding) {
            if (surroundingPuzzle != null
                    && !stateByPuzzle.containsKey(surroundingPuzzle)) {
                int scoreFnValue = getScoreFnValue(surroundingPuzzle);
                State state = new State(surroundingPuzzle, stateByPuzzle.get(puzzle), scoreFnValue);
                stateByPuzzle.put(surroundingPuzzle, state);
            }
        }
    }

    private int getScoreFnValue(Puzzle puzzle) {
        int result = 0;
        int sideLength = puzzle.getSideLength();
        for (int x = 0; x < sideLength; x++) {
            for (int y = 0; y < sideLength; y++) {
                Point point = new Point(x, y);
                int piece = puzzle.getPieceAt(point);
                Point correctPoint = pieceCorrectPlacement.get(piece);
                result += point.getRectangularDistance(correctPoint);
            }
        }
        return result;
    }
}