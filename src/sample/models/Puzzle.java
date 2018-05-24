package sample.models;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Mateusz Grabarski on 24.05.2018.
 */
public class Puzzle {

    private int[][] pieces;
    private Point emptySpace;

    public Puzzle(int sideLength) {
        pieces = new int[sideLength][sideLength];
        for (int x = 0; x < sideLength; x++) {
            for (int y = 0; y < sideLength; y++) {
                pieces[x][y] = sideLength * y + x;
            }
        }
        emptySpace = new Point(0, 0);
    }

    public Puzzle(Puzzle puzzle) {
        int sideLength = puzzle.pieces.length;
        pieces = new int[sideLength][sideLength];
        for (int x = 0; x < sideLength; x++) {
            for (int y = 0; y < sideLength; y++) {
                pieces[x][y] = puzzle.pieces[x][y];
            }
        }
        emptySpace = puzzle.emptySpace;
    }

    public Puzzle moveUp() {
        Point pointToBeMoved = new Point(emptySpace.getX(), emptySpace.getY() + 1);
        return move(pointToBeMoved);
    }

    public Puzzle moveDown() {
        Point pointToBeMoved = new Point(emptySpace.getX(), emptySpace.getY() - 1);
        return move(pointToBeMoved);
    }

    public Puzzle moveLeft() {
        Point pointToBeMoved = new Point(emptySpace.getX() + 1, emptySpace.getY());
        return move(pointToBeMoved);
    }

    public Puzzle moveRight() {
        Point pointToBeMoved = new Point(emptySpace.getX() - 1, emptySpace.getY());
        return move(pointToBeMoved);
    }

    private Puzzle move(Point pointToBeMoved) {
        if (contains(pointToBeMoved)) {
            Puzzle puzzle = new Puzzle(this);
            int piece = puzzle.pieces[pointToBeMoved.getX()][pointToBeMoved.getY()];
            puzzle.pieces[pointToBeMoved.getX()][pointToBeMoved.getY()] = puzzle.pieces[emptySpace.getX()][emptySpace.getY()];
            puzzle.pieces[emptySpace.getX()][emptySpace.getY()] = piece;
            puzzle.emptySpace = pointToBeMoved;
            return puzzle;
        } else {
            return null;
        }
    }

    public Puzzle shuffle(int moves) {
        Puzzle result = this;
        Random random = new Random();
        for (int i = 0; i < moves; i++) {
            int direction = random.nextInt(4);
            Puzzle afterMovePuzzle = null;
            switch (direction) {
                case 0:
                    afterMovePuzzle = result.moveUp();
                    break;
                case 1:
                    afterMovePuzzle = result.moveDown();
                    break;
                case 2:
                    afterMovePuzzle = result.moveLeft();
                    break;
                case 3:
                    afterMovePuzzle = result.moveRight();
                    break;
            }
            if (afterMovePuzzle != null) {
                result = afterMovePuzzle;
            } else {
                i--;
            }
        }
        return result;
    }

    private boolean contains(Point point) {
        int sideLength = pieces.length;
        return point.getX() < sideLength && point.getY() < sideLength
                && point.getX() >= 0 && point.getY() >= 0;
    }

    public int getSideLength() {
        return pieces.length;
    }

    public int getPieceAt(Point point) {
        return pieces[point.getX()][point.getY()];
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int sideLength = pieces.length;
        for (int y = 0; y < sideLength; y++) {
            for (int x = 0; x < sideLength; x++) {
                int piece = pieces[x][y];
                if (piece < 10) {
                    stringBuilder.append(' ');
                }
                stringBuilder.append(piece);
                stringBuilder.append(' ');
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Puzzle)) return false;

        Puzzle puzzle = (Puzzle) o;

        if (!Arrays.deepEquals(pieces, puzzle.pieces)) return false;
        return emptySpace != null ? emptySpace.equals(puzzle.emptySpace) : puzzle.emptySpace == null;
    }

    @Override
    public int hashCode() {
        int result = Arrays.deepHashCode(pieces);
        result = 31 * result + (emptySpace != null ? emptySpace.hashCode() : 0);
        return result;
    }
}