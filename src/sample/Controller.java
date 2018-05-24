package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import sample.models.Point;
import sample.models.Puzzle;
import sample.solver.PuzzleSolver;

import java.util.List;

public class Controller {

    @FXML
    private TextField numberOfElements;

    @FXML
    private Button refresh;

    @FXML
    private GridPane puzzleGridPane;

    @FXML
    private Button shuffle;

    @FXML
    private Button resolve;

    private int sideLength;
    private Puzzle puzzle;
    private TextField[][] textFields;

    public void initialize() {
        updateSideLength();

        refresh.setOnAction(event -> updateSideLength());
        shuffle.setOnAction(event -> setPuzzle(puzzle.shuffle(100)));
        resolve.setOnAction(event -> {
            List<Puzzle> steps = new PuzzleSolver(new Puzzle(sideLength), puzzle).solve();
            new Thread(() ->
                    steps.forEach(s -> {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(() -> setPuzzle(s));
                    })).start();
        });
    }

    private void updateSideLength() {
        puzzleGridPane.getChildren().clear();
        sideLength = Integer.parseInt(numberOfElements.getText());
        textFields = new TextField[sideLength][sideLength];
        for (int x = 0; x < sideLength; x++) {
            for (int y = 0; y < sideLength; y++) {
                TextField textField = new TextField();
                textFields[x][y] = textField;
                puzzleGridPane.add(textField, x, y);
            }
        }
        setPuzzle(new Puzzle(sideLength));
    }

    private void setPuzzle(Puzzle puzzle) {
        if (puzzle != null) {
            this.puzzle = puzzle;
            displayPuzzle();
        }
    }

    private void displayPuzzle() {
        for (int x = 0; x < sideLength; x++) {
            for (int y = 0; y < sideLength; y++) {
                int piece = puzzle.getPieceAt(new Point(x, y));
                if (piece == 0) {
                    textFields[x][y].clear();
                } else {
                    textFields[x][y].setText(String.valueOf(piece));
                }
            }
        }
    }
}
