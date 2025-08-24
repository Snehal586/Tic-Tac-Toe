
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class TicTac extends JFrame {
    private final JButton[] buttons = new JButton[9];
    private final JLabel statusLabel = new JLabel("Choose difficulty to start");
    private final JButton resetButton = new JButton("Reset");
    private final JComboBox<String> difficultyBox = new JComboBox<>(new String[]{"Easy", "Hard"});
    private char human = 'X';
    private char computer = 'O';
    private boolean gameOver = false;

    public TicTac() {
        setTitle("Tic Tac Toe - Human vs AI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(400, 450);
        setLocationRelativeTo(null);

        // Board Panel
        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 9; i++) {
            final int idx = i;
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font("Arial", Font.BOLD, 40));
            buttons[i].setFocusPainted(false);
            buttons[i].addActionListener(e -> playerMove(idx));
            boardPanel.add(buttons[i]);
        }

        // Control Panel
        JPanel controlPanel = new JPanel();
        controlPanel.add(new JLabel("Difficulty:"));
        controlPanel.add(difficultyBox);
        resetButton.addActionListener(e -> resetGame());
        controlPanel.add(resetButton);

        // Status Panel
        JPanel statusPanel = new JPanel();
        statusPanel.add(statusLabel);

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.NORTH);
        add(statusPanel, BorderLayout.SOUTH);
    }

    private void playerMove(int idx) {
        if (gameOver || !buttons[idx].getText().equals("")) return;
        buttons[idx].setText(String.valueOf(human));
        if (checkGameOver()) return;
        computerMove();
    }

    private void computerMove() {
        ArrayList<Integer> available = new ArrayList<>();
        for (int i = 0; i < 9; i++) if (buttons[i].getText().equals("")) available.add(i);

        if (available.isEmpty()) return;

        int move;
        if (difficultyBox.getSelectedItem().equals("Easy")) {
            // random
            move = available.get(new Random().nextInt(available.size()));
        } else {
            // minimax
            move = bestMove();
        }

        buttons[move].setText(String.valueOf(computer));
        checkGameOver();
    }

    private boolean checkGameOver() {
        String winner = getWinner();
        if (!winner.equals("")) {
            statusLabel.setText("Winner: " + winner);
            gameOver = true;
            return true;
        } else if (isFull()) {
            statusLabel.setText("It's a Draw!");
            gameOver = true;
            return true;
        }
        return false;
    }

    private boolean isFull() {
        for (JButton b : buttons) if (b.getText().equals("")) return false;
        return true;
    }

    private String getWinner() {
        int[][] lines = {
            {0,1,2},{3,4,5},{6,7,8},
            {0,3,6},{1,4,7},{2,5,8},
            {0,4,8},{2,4,6}
        };
        for (int[] L : lines) {
            String a = buttons[L[0]].getText();
            String b = buttons[L[1]].getText();
            String c = buttons[L[2]].getText();
            if (!a.equals("") && a.equals(b) && b.equals(c)) return a;
        }
        return "";
    }

    private void resetGame() {
        for (JButton b : buttons) b.setText("");
        statusLabel.setText("Game reset. Your turn!");
        gameOver = false;
    }

    /* ===================== MINIMAX ===================== */
    private int bestMove() {
        int bestScore = Integer.MIN_VALUE;
        int move = -1;
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().equals("")) {
                buttons[i].setText(String.valueOf(computer));
                int score = minimax(false);
                buttons[i].setText("");
                if (score > bestScore) {
                    bestScore = score;
                    move = i;
                }
            }
        }
        return move;
    }

    private int minimax(boolean isMaximizing) {
        String winner = getWinner();
        if (winner.equals(String.valueOf(computer))) return 10;
        if (winner.equals(String.valueOf(human))) return -10;
        if (isFull()) return 0;

        int bestScore = isMaximizing ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        char current = isMaximizing ? computer : human;

        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().equals("")) {
                buttons[i].setText(String.valueOf(current));
                int score = minimax(!isMaximizing);
                buttons[i].setText("");

                if (isMaximizing) bestScore = Math.max(score, bestScore);
                else bestScore = Math.min(score, bestScore);
            }
        }
        return bestScore;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TicTac().setVisible(true);
        });
    }
}


