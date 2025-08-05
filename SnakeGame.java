import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class SnakeGame extends JPanel implements ActionListener, KeyListener{

    private class Tile {
        int y;
        int x;

        Tile (int x , int y){
            this.x=x;
            this.y=y;

        }
    }


    int boardWidth;
    int boardHeight;
    int tileSize = 25;

    //snake 
    Tile snakeHead;
    ArrayList<Tile> snakeBody; //defining a arraylist

    //food
    Tile food;

    //random 
    Random random;

    //game logic
    Timer gameLoop;
    int velocityX;
    int velocityY;
    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);  
        //making game listen to key presses
        addKeyListener(this); 
        setFocusable(true);

        snakeHead = new Tile(5,5);

        //arraylist to store all the snake's body parts
        snakeBody = new ArrayList<Tile>();

        food = new Tile(10,10);

        random = new Random();
        placeFood();

        //to move the snake move
        velocityX = 0;
        velocityY = 0;

        gameLoop = new Timer (100, this);
        gameLoop.start();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw (Graphics g){
        //grid
       // for(int i = 0; i < boardWidth/tileSize; i++){
        //    //x1, y1 , x2, y2
        //    g.drawLine (i*tileSize, 0 , i*tileSize, boardHeight);
        //    g.drawLine (0,i*tileSize, boardWidth, i*tileSize);
       // }

        //draw Food
        g.setColor(Color.red);
        //g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);

        //draw snake head
        g.setColor(Color.green);
        //g.fillRect(snakeHead.x * tileSize,snakeHead.y * tileSize, tileSize, tileSize);
        g.fill3DRect(snakeHead.x * tileSize,snakeHead.y * tileSize, tileSize, tileSize,true);

        //draw snake body
        for(int i = 0; i < snakeBody.size(); i++){
            Tile snakePart = snakeBody.get(i);
            //g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize,true);
            }

            //score 
            g.setFont(new Font ("Arial", Font.PLAIN, 16));
            if(gameOver){
                g.setColor(Color.red);
                g.drawString("Game Over: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
            }
            else{
                g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);      
            }
        }

        public void placeFood(){
            food.x = random.nextInt(boardWidth/tileSize); //600/25 = 24
            food.y = random.nextInt(boardHeight/tileSize);
        }

        //method to detect collision
        public boolean collision (Tile tile1, Tile tile2){
            return tile1.x == tile2.x && tile1.y == tile2.y;
            
        }

        public void move(){
            //eat food
            if(collision(snakeHead, food)){
                snakeBody.add(new Tile(food.x, food.y));
                placeFood();

            }

            //Shake Body
            for(int i = snakeBody.size()-1; i >= 0; i--){
                Tile snakePart = snakeBody.get(i);
                if(i == 0){
                    snakePart.x = snakeHead.x;
                    snakePart.y = snakeHead.y;
                }
                else{
                    Tile prevSnakePart = snakeBody.get(i-1);
                    snakePart.x = prevSnakePart.x;
                    snakePart.y = prevSnakePart.y;
                }
            }

            //shake head
            snakeHead.x += velocityX;
            snakeHead.y += velocityY;

            //game over conditions
            //1. snake collides with its own body
            for(int i = 0; i < snakeBody.size(); i++){
                Tile snakePart = snakeBody.get(i);
                //collide with the snake head
                if(collision(snakeHead, snakePart)){
                    gameOver = true;
                }
            }
            //2. hit one of the 4 walls
            if(snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth ||
                snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight){
                gameOver = true;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            move ();
            repaint(); //evey 100 millinsecond the repaint action will be called

            //will end the game if the condition gameover is true;
            if(gameOver){
                gameLoop.stop();
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
           //defining the keys needed
           if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1){
                velocityX = 0;
                velocityY = -1;
           }
           else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1){
                velocityX = 0;
                velocityY = 1;
           }
           else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1){
                velocityX = -1;
                velocityY = 0;
           }
           else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1){
                velocityX = 1;
                velocityY = 0;
           }
        }

        //do not need
        @Override
        public void keyTyped(KeyEvent e){
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

