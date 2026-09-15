import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        
    // 1. Create a window for the game
        int boardWidth = 600;
        int boardHeight = boardWidth;

        // Define a frame
        JFrame frame = new JFrame("Snake v0.1");
        frame.setVisible(true);                  // make frame visible
        frame.setSize(boardWidth, boardHeight);    // frame resolution
        frame.setLocationRelativeTo(null);       // spawn the fame in the middle of the screen
        frame.setResizable(false);       // not resizable
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   // close the window after clicking on X

        // Calling SnakeGame class here with the object
        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
        // Adding snakeGame object into the frame we have created
        frame.add(snakeGame);
        // To make the widow-size what we intended without title bar of the window
        frame.pack();
        // Make SnakeGame to request key presses
        snakeGame.requestFocus();
    }
}
