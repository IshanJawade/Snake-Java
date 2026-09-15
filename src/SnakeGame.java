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
    Tile food;      // food

    // Game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;

    // Contructor 
    SnakeGame(int boardWidth, int boardHeight){

        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        // Make a JPanel of the disired diamention
        setPreferredSize( new Dimension(this.boardWidth, this.boardHeight) );
        setBackground(Color.BLACK);
        addKeyListener(this);

        // One example of the default position of the snakeHead and the food
        snakeHead = new Tile(5,5);
        food = new Tile();

        random = new Random();
        placeFood();    // give random values to food

        velocityX = 0;
        velocityY = -1;

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
        
        // Draw snake:
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);
        
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

    public void move(){
        
        // Snake head:
        // Adding the number feels like velocity 
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        move();
        repaint();
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        
        // UP 
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            velocityX = 0;
            velocityY = -1;
        } 
        // DOWN
        else if(e.getKeyCode() == KeyEvent.VK_DOWN){
            velocityX = 0;
            velocityY = 1;
        }
        // LEFT
        else if(e.getKeyCode() == KeyEvent.VK_LEFT){
            velocityX = -1;
            velocityY = 0;
        }
        // RIGHT
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT){
            velocityX = 1;
            velocityY = 0;
        }
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
