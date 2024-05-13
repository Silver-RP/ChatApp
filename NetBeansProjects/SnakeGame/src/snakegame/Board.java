
package snakegame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Board extends JPanel implements ActionListener{
    private Image apple;
    private Image head;
    private Image dot;
    private Timer timer;
    
    private final int ALL_DOT = 900;
    private final int DOT_SIZE = 10;
    private final int RANDOM_POSITION = 29;
    
    private int apple_x;
    private int apple_y;
    
    private final int x[] = new int[ALL_DOT];
    private final int y[] = new int[ALL_DOT];
    
    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection =false;
    private boolean downDirection = false;
    
    private boolean inGame = true;
    
    private int dots;
    
    
    Board(){
        addKeyListener(new TAdapter());
        
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(300, 300));
        
        loadImages();
        initGame();
        
    }
    
    public void loadImages(){
        try {
            apple = new ImageIcon(getClass().getResource("/icons/apple.png")).getImage();
            head = new ImageIcon(getClass().getResource("/icons/head.png")).getImage();
            dot = new ImageIcon(getClass().getResource("/icons/dot.png")).getImage();
            System.out.println("Images loaded successfully.");
        } catch (NullPointerException e) {
            System.err.println("Error loading images: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void initGame(){
        dots = 3;
        
        for(int i=0; i<dots; i++){
            y[i] = 50;
            x[i] = 50 - i*DOT_SIZE;
        }
        
        locateApple();
        timer = new Timer(140 ,this);
        timer.start();
    }
    
    public void locateApple(){
        int r = (int) (Math.random()* RANDOM_POSITION);
        apple_x = r* DOT_SIZE;
        
        r = (int) (Math.random()* RANDOM_POSITION);
        apple_y = r* DOT_SIZE ;
        System.out.print("apple_x: " + apple_x+ ", apple_y: " + apple_y +"\n");
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    
    public void draw(Graphics g){
        if(inGame){
            g.drawImage(apple, apple_x, apple_y, this);
            for ( int i = 0; i < dots; i++){
                if(i == 0){
                    g.drawImage(head, x[i], y[i], this);
                }else{
                    g.drawImage(dot, x[i], y[i], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        }else{
            gameOver(g);
        }
        
    }
    
    public void gameOver(Graphics g){
        String msg = "Game Over";
        Font f = new Font("SAN_SERIF", Font.BOLD, 14);
        FontMetrics fm = getFontMetrics(f);
        
        g.setColor(Color.WHITE);
        g.drawString(msg, (300 - fm.stringWidth(msg))/ 2, 300 / 2);
    }
    
    public void move(){
        for(int i = dots; i > 0; i--){
            x[i]= x[i-1];
            y[i]= y[i-1];
        }
        
        if(leftDirection){
            x[0] = x[0] - DOT_SIZE;
        }
        if(rightDirection){
            x[0] = x[0] + DOT_SIZE;
        }
        if(upDirection){
            y[0] = y[0] - DOT_SIZE;
        }
        if(downDirection){
            y[0] = y[0] + DOT_SIZE;
        }
    }
    
    public void checkApple(){
        if(x[0] == apple_x && y[0] == apple_y){
            dots ++;
            locateApple();
        }
    }
    
    public void checkCollistion(){
        for (int i = dots; i > 0; i --){
            if(i > 4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }
        if(y[0] >= 300){
            inGame = false;
        }
        if(x[0] >= 300){
            inGame = false;
        }
        if(y[0] < 0 ){
            inGame = false;
        }
        if(x[0] < 0 ){
            inGame = false;
        }
        
        if(!inGame){
            timer.stop();
        }
    }
    
    public void actionPerformed(ActionEvent ae){
        if(inGame){
            checkApple();
            checkCollistion();
            move();
        }
        
        repaint();
    }
    
    public class TAdapter extends KeyAdapter{
       @Override
       public void keyPressed(KeyEvent e){
           int key = e.getKeyCode();
           
           if(key == KeyEvent.VK_LEFT && (!rightDirection)){
               leftDirection = true;
               upDirection = false;
               downDirection = false;
           }
           if(key == KeyEvent.VK_RIGHT && (!leftDirection)){
               rightDirection = true;
               upDirection = false;
               downDirection = false;
           }
           if(key == KeyEvent.VK_UP && (!downDirection)){
               upDirection = true;
               rightDirection = false;
               leftDirection = false;
           }
           if(key == KeyEvent.VK_DOWN && (!upDirection)){
               downDirection = true;
               rightDirection = false;
               leftDirection = false;
           }
       }
        
    }
    
}