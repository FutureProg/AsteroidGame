package AsteroidGame;

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.net.URL;
import javax.sound.sampled.*;
import java.applet.AudioClip;

public class AsteroidPanel extends JPanel{
  private int screenW,screenH;
  boolean started;
  int gameScore,hits,increases;
  Player gamePlayer;
  Timer timer;
  long startTime;//When the game started
  boolean rightKey,leftKey,upKey,downKey;
  ArrayList<RocketMissile> rockets;
  ArrayList<Meteor> meteors;
  private int meteorRate;//1 out of x chance of meteors.
  private final int ROCKET_SPEED = 3;
  boolean focused,gameOver;//gameOver and whether its focused.
  AudioClip explSound, shootSound;
  
  public AsteroidPanel(){
    started = false;
    setBackground(Color.BLACK);
    requestFocus();
    rightKey = false;
    leftKey = false;
    meteorRate = 70;
    gameScore = 0;
    increases = 0;
    hits = 0;
    rockets = new ArrayList<RocketMissile>();
    meteors = new ArrayList<Meteor>();
    focused = false;
    gameOver = false;
    
    /*Timer*/
    timer = new Timer(30,new ActionListener(){
      public void actionPerformed(ActionEvent e){
        if (gamePlayer != null){
          meteors.add(new Meteor());
        }
        gameScore = (int)(System.currentTimeMillis()-startTime);
        /*To test whether a is divisible by b without using a % b, you could do the following:
         * boolean divisible = (a / b * b == a);
         */ 

        if ((hits==10 && increases == 0)  || (hits== 20 && increases == 1) || (hits==30 && increases == 2) ||(hits==40 && increases == 3) ||( hits == 50 && increases == 4) ||( hits==60 && increases == 5) || (hits == 70 && increases == 6) ||( hits==80 && increases == 7) || (hits == 90  && increases == 8)|| (hits==100 && increases == 9) || (hits== 110 && increases == 10)){
            meteorRate-= 5;
            increases++;
            System.out.println("reached even");
        }
        
        for (int i = 0; i < meteors.size(); i++){
          Meteor j = meteors.get(i);
          if (j.isCollision(gamePlayer)){
            System.err.println("GAME OVER");
            gameOver = true;
            repaint();
            timer.stop();
          }
        }
        repaint();
      }
    });
    
    /**
     * Create the key responses.
     * left decreases x
     * right increases x
     */
    addKeyListener(new KeyAdapter(){
      public void keyPressed(KeyEvent e){
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT){
          if (gamePlayer != null)
            leftKey = true;
        }else if(code == KeyEvent.VK_RIGHT){
          if (gamePlayer != null)
            rightKey = true;
        }else if(code == KeyEvent.VK_UP){
          if(gamePlayer != null)
            upKey = true;
        }else if(code == KeyEvent.VK_DOWN){
          if(gamePlayer != null)
            downKey = true;
        }
        
      }
      
      public void keyReleased(KeyEvent e){
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_LEFT)
          leftKey = false;
        if (code == KeyEvent.VK_RIGHT) 
          rightKey = false;
        if (code == KeyEvent.VK_SPACE){
          rockets.add(new RocketMissile());
          shootSound.play();
          //System.out.println("VK_SPACE");
        }
        if (code == KeyEvent.VK_UP)
          upKey = false;
        if(code == KeyEvent.VK_DOWN)
          downKey = false;
          
        
      }
      
    });
    
    /*Mouse Listener*/
    addMouseListener(new MouseAdapter(){
      public void mousePressed(MouseEvent e){
        requestFocus();
        System.out.println("Mouse PRessed");
      }
    });
    
    /* Focus Listener*/
    addFocusListener(new FocusListener(){
      public void focusLost(FocusEvent e){
        System.out.println("Focus Lost");
        focused = false;
      }
      
      public void focusGained(FocusEvent e){
        System.out.println("Focus Gained");
        focused = true;
        if(!started){
          startTime = System.currentTimeMillis();
          started = true;
        }
        timer.start();
      }
    });
    
    /*Create AUDIO*/
    JApplet soundMaster = new JApplet();
    File explFile = new File("AsteroidGame/Audio/SE/cannon.wav");
    File shootFile = new File("AsteroidGame/Audio/SE/lasergun1.wav");
    if(!explFile.exists())
      System.err.println("cannon.wav doesn't exist!");
    if(!shootFile.exists())
      System.err.println("lasergun1.wav doesn't exist!");
    
    ClassLoader cl = AsteroidPanel.class.getClassLoader();
    URL resourceURL = cl.getResource("AsteroidGame/Audio/SE/cannon.wav");
    if(resourceURL != null){
       explSound = soundMaster.newAudioClip(resourceURL);
    }
    resourceURL = cl.getResource("AsteroidGame/Audio/SE/lasergun1.wav");
    if(resourceURL != null)
      shootSound = soundMaster.newAudioClip(resourceURL);
      
  }//End CONSTRUCTOR
  
  public void paintComponent(Graphics g){
    /*Get the background image*/
      File bgImageFile = new File("AsteroidGame/Graphics/background.png");
      if(!bgImageFile.exists()){
        System.err.println("background sprite doesn't exist!");
        return;
      }
      try{
        BufferedImage bgImage = ImageIO.read(bgImageFile);
        g.drawImage(bgImage,0,0,null);
      }catch(IOException e){
        System.err.println(e.getMessage());
        return;
      }  
    if (gamePlayer == null)
      gamePlayer = new Player();
    if(rightKey)
      gamePlayer.x += ROCKET_SPEED;
    if(leftKey)
      gamePlayer.x -= ROCKET_SPEED;
    if(upKey)
      gamePlayer.y -= ROCKET_SPEED+2;
    if(downKey)
      gamePlayer.y += ROCKET_SPEED+2;
    if(!rightKey && !leftKey && !upKey && !downKey && gamePlayer.y != gamePlayer.originalY)
      gamePlayer.y += 1;
    if (!rockets.isEmpty()){
      for(int i = 0; i < rockets.size();i++){
        RocketMissile j = (RocketMissile)rockets.get(i);
        j.y -= 5;
        j.draw(g);
        if(j.destroyed){
          rockets.remove(j);
          j = null;     
        }
      }
    }
    if (!meteors.isEmpty()){
      for (int i = 0; i< meteors.size(); i++){
        Meteor j = (Meteor)meteors.get(i);
        if (!j.destroyed){
          j.y +=j.speed;
          j.draw(g);
        }else{
          j.x = -20;
          j.y = -20;
          j=null;
        }
      }
    }
    
    gamePlayer.draw(g);
    
    g.setColor(Color.WHITE);
    g.drawString("SCORE: " + gameScore,30,30);
    
    if (!focused){
      g.setColor(Color.RED);
      g.drawString("CLICK TO START",250,250);
    }
    if (gameOver){
      g.setColor(Color.RED);
      g.drawString("GAME OVER",250,250);
      JFrame screen = new JFrame("GAME OVER");
      screen.setSize(500,500);
      try{
        screen.setContentPane(new HighScoreTable(gameScore,hits));
      }catch(IOException e){
        System.err.println(e.getMessage());
      }
      screen.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
      screen.setVisible(true);
      SwingUtilities.getWindowAncestor(this).setVisible(false); 
    }
     
  }
  
  
  /*
  public class MoveShip extends Thread{
    public void run(){
      if (leftKey)
        gamePlayer.x -= ROCKET_SPEED;
      if (rightKey)
        gamePlayer.x += ROCKET_SPEED;
    }
  }
  */
  
  
  /**
   * Create the player class
  */
  public class Player{
    public int x, y, originalY,hp;
    protected int width, height,moveWidth,moveHeight;
    BufferedImage img,imgMove;
    
    public Player(){
      hp = 3;
      /*Get the image*/
      File rocketImg = new File("AsteroidGame/Graphics/rocketCut.png");
      File rocketMoveImg = new File("AsteroidGame/Graphics/rocketMove.png");
      if (!rocketImg.exists() && !rocketMoveImg.exists()){
        System.err.println("Rocket sprite doesn't exist!");
        return;
      }
      
      try{
        img = ImageIO.read(rocketImg);
        imgMove = ImageIO.read(rocketMoveImg);
        System.out.println("Got image");
      }catch(IOException e){
        System.out.println(e.getMessage());
        return;
      }//got the image.
      
      height = img.getHeight();
      width = img.getWidth();
      x = (getWidth()/2)-(width/2);
      y = getHeight()-height-10;
      originalY = y;
      
      moveHeight = imgMove.getHeight();
      moveWidth = imgMove.getWidth();
    }
    
    public void draw(Graphics g){
      if (!upKey){
        //if(x>0 && y>0 && x+60<500 && y+48<500)
        if(x<0)
          x = 0;
        if(y<0)
          y=0;
        if(x+60>500)
          x= 500-60;
        if(y>originalY)
          y = originalY;
        g.drawImage(img,x,y,null);
        }else{
          g.drawImage(imgMove,x,y,null);
        }
    }
    
  }
  
  /*END OF PLAYER CLASS*/
  
  /**
   * Create the Missiles for the rocket.
   */
  public class RocketMissile{
    public int x,y;
    boolean destroyed;
    
    public RocketMissile(){
      if (gamePlayer != null){
        x = gamePlayer.x+((gamePlayer.img.getWidth())/2)-10;
        y = gamePlayer.y-20;
      }
    }
    
    public void draw(Graphics g){
      for (int i = 0; i < meteors.size(); i++){
        Meteor j = meteors.get(i);
        if (j.isCollision(this) && !destroyed){
          destroyed = true;
        }
        if(!destroyed && y<=-20)
          destroyed = true;
      }
      g.setColor(Color.RED);
      if(!destroyed)
        g.fillOval(x,y,20,20);
      
    }
    
     
  }
  /*END OF ROCKETMISSILE CLASS*/
  
  
  /**
   * Creates the Meteors
  */
  public class Meteor{
    public int x, y,hp,maxHP,speed;
    private final int WIDTH = 40;
    private final int HEIGHT = 40;
    BufferedImage img;
    public boolean destroyed;
    
    public Meteor(){
      destroyed = false;
      /*Get the image*/
      File meteorImage = new File("AsteroidGame/Graphics/meteorites.png");
      if(!meteorImage.exists()){
        System.err.println("Meteor sprite doesn't exist!");
        return;
      }
      try{
        img = ImageIO.read(meteorImage);
      }catch(IOException e){
        System.err.println(e.getMessage());
        return;
      }
      
      x = -40;
      y = -40;
      Random rand = new Random();
      //int randomNum = rand.nextInt(max - min + 1) + min;
      int randomNum = rand.nextInt(meteorRate-1+1)+1;
      if(randomNum == 1 || randomNum ==2){
        randomNum = rand.nextInt(3-1+1)+1;
        if(randomNum == 2){
          hp = 3;
          maxHP = 3;
        }else if (randomNum == 3){
          hp = 1;
          maxHP = 1;
        }else{
          hp = 5;
          maxHP = 5;
        }
        x = rand.nextInt(460-1+1)+1;
        speed = rand.nextInt(7-2+1)+2;
      }
    }
    
    public void draw(Graphics g){
      if(y > 500)
        destroyed = true;
      if(!destroyed){
        if (maxHP == 1)
          g.drawImage(img,x,y-40,x+40,y,0,0,40,40,null);
        else if (maxHP == 3)
          g.drawImage(img,x,y-40,x+40,y,74,110,74+40,150,null);
        else if (maxHP == 5)
          g.drawImage(img,x,y-40,x+40,y,233,110,233+40,150,null);
      }
    }
    
    public boolean isCollision(Player gamePlayer){
      if(this.x>gamePlayer.x-10 && this.x<gamePlayer.x+20 && this.y<gamePlayer.y+40 && this.y+20>gamePlayer.y && this.y > 0 && this.y < 500 && !destroyed){
        explSound.play();
        return true;
      }else{
        return false;
      }
    }
    public boolean isCollision(RocketMissile playerRocket){
      if(this.x>playerRocket.x-40 && this.x<playerRocket.x+20 && this.y<playerRocket.y+20 && this.y>playerRocket.y & !destroyed && !playerRocket.destroyed){
        hp--;
        if (hp <= 0){
          destroyed = true;
          hits++;
          explSound.play();
          System.gc();
        }
        return true;
      }else{
        return false;
      }
    }
  }
  
  public boolean isGameOver(){
      return gameOver;
  }
}
  