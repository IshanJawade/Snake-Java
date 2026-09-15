import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class SnakeGame extends JPanel implements ActionListener, KeyListener{

    // Divide the window in 24x24 tiles to move the snake
    // Each tile is 25x25 pixels 
    private class Tile{
        int x, y;

        Tile(){
            this.x = 0;
            this.y = 0;    
        }

        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }

    }

    int boardWidth, boardHeight;
    int tileSize = 25;      // each tile size
    Random random;
    
    // Tile class
    Tile snakeHead; // snake
    ArrayList<Tile> snakeBody;
    Tile food;      // food

    // Game logic
    Timer gameLoop;
    boolean gameOver;
    int velocityX;
    int velocityY;

    // Contructor : Initializes the game 
    SnakeGame(int boardWidth, int boardHeight){

        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        // Make a JPanel of the disired diamention
        setPreferredSize( new Dimension(this.boardWidth, this.boardHeight) );
        setBackground(Color.BLACK);
        addKeyListener(this);

        // Initialization 
        gameOver = false;
        snakeHead = new Tile(1,0);  // Snake's default spawn position
        food = new Tile();
        snakeBody = new ArrayList<Tile>();
        snakeBody.add(new Tile());

        random = new Random();
        placeFood();    // give random values to food

        // Snake default move direction after spawn
        velocityX = 1;
        velocityY = 0;

        //  The timer that runs game loop (tik)
        gameLoop = new Timer(128, this);
        gameLoop.start();

    }

    // Built-In Swing mwthod to call when JPlane renders for the firs time
    @Override 
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    
    // To draw things on the screen
    public void draw(Graphics g){
        
        // Draw grid lines: 
        // change the color for the gridlines on the black baground by default it's black
        g.setColor(Color.DARK_GRAY);    
        for(int i=0; i<boardWidth * tileSize; i++){
            //   from , to   
            // (x1, y1, x2, y2)
            g.drawLine(i*tileSize,0 , i*tileSize, boardHeight);   // horizontal
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize);    // vertical 
        }
        
        // Draw food
        g.setColor(Color.RED);
        g.fillRect(food.x * tileSize, food.y*tileSize, tileSize, tileSize);
        
        // Draw snake head:
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        // Draw snake body
        for(int i=0; i<snakeBody.size(); i++){

            // Take one square
            Tile snakePart = snakeBody.get(i);
            // draw this part
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }
        
        // Draw score
        g.setFont( new Font("Arial", Font.PLAIN, 16));

        // after game over
        if(gameOver){
            g.setColor(Color.RED);
            g.drawString("Game Over!",16, 25);
            g.setColor(Color.WHITE);
            g.drawString("Score: " + String.valueOf(snakeBody.size() - 1), 16, 50);
        }
        // to show the score while game is going on 
        else {
            g.setColor(Color.GREEN);
            g.drawString("Score: " + String.valueOf(snakeBody.size() - 1), 6, 25);
        }
    }

    // Place food randomly on the grid
    public void placeFood() {
        /* Logic : 
            600 / 25 = 24
            So random() will generate random values between 0 - 24 
        */
        food.x = random.nextInt(boardWidth / tileSize);    
        food.y = random.nextInt(boardHeight / tileSize);
    }

    // To detect collision 
    public boolean collision(Tile tile1, Tile tile2){

        // If 2 tiles have same position then thats the Collision!
        return tile1.x == tile2.x && tile1.y == tile2.y; 
    }

    // Update the moves on every tik
    public void move(){

        // Eat Food : check collision between snakehead and food 
        if(collision(snakeHead, food)){

            snakeBody.add(new Tile(food.x, food.y));
            placeFood();    // spawn new random food
        }

        // Snake Body:
        for(int i=snakeBody.size()-1; i>=0; i--){

            Tile snakePart = snakeBody.get(i);
            // if the part is just behind the head
            if(i==0){
                snakePart.x = snakeHead.x;
                snakePart.y = snakeHead.y;
            } 
            // For all the other parts
            else {
                Tile preSnakePart = snakeBody.get(i-1);
                snakePart.x = preSnakePart.x;
                snakePart.y = preSnakePart.y;
            }
        }

        // Snake Head:
        // Adding the number, feels like velocity 
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        // Game over conditions:
        // 1) If snake head collides with it's body 
        for(int i=0; i<snakeBody.size(); i++){

            Tile snakePart = snakeBody.get(i);
            if(collision(snakeHead, snakePart)){
                gameOver = true;
            }
        }
        // 2) If snake collides with the wall (edges of the windows)
        if(snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth ||
            snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight)
        {
            gameOver = true;
        } 

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        move();
        
        // Only repaint the JPlane when game is on 
        // to get that instantenuos stoppage after collision
        if(gameOver){
            gameLoop.stop();
        } 
        repaint();
    }
    
    // Map key presses to snake changing directions
    @Override
    public void keyPressed(KeyEvent e) {
        /* 
            Make key presses change the direction od the snake 
            And make sure the snake should not go the completelt opposite direction its
            already going on. Ex: Can't go directly right if the snake is going left.
         */
        // UP 
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } 
        // DOWN
        else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
            velocityX = 0;
            velocityY = 1;
        }
        // LEFT
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
            velocityX = -1;
            velocityY = 0;
        }
        // RIGHT
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
            velocityX = 1;
            velocityY = 0;
        }
        // Restart instatnt for debugging
        // else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
        //     SnakeGame(boardWidth, boardHeight);
        // }
    }
    
    /*
        We don't need these methods!!
    */
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
