package sample.models;

/**
 * Created by Mateusz Grabarski on 24.05.2018.
 */
public class State {

    private Puzzle puzzle;
    private State origin;
    private int scoreFnValue;
    private boolean visited;

    public State(Puzzle puzzle, State origin, int scoreFnValue) {
        this.puzzle = puzzle;
        this.origin = origin;
        this.scoreFnValue = scoreFnValue;
        this.visited = false;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public State getOrigin() {
        return origin;
    }

    public int getScoreFnValue() {
        return scoreFnValue;
    }

    public boolean isVisited() {
        return visited;
    }

    public void visit() {
        this.visited = true;
    }
}