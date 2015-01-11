package AsteroidGame;

import javax.swing.Timer;
import javax.swing.JFrame;
import javax.swing.JApplet;
import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStream.*;
import java.io.File;
import java.net.URL;
import javax.sound.sampled.*;

public class AsteroidGame{
  
  
  public static void main(String[] args){ 
    JFrame mainFrame = new JFrame("Asteroid Game");
    mainFrame.setSize(500,500);
    AsteroidPanel asteroidPanel = new AsteroidPanel();
    mainFrame.setContentPane(asteroidPanel);   
    mainFrame.setResizable(false);
    mainFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    
    JApplet soundMaster = new JApplet();
    File bgmFile = new File("AsteroidGame/Audio/BGM/A Serious Mission.mp3");
    if(!bgmFile.exists())
      System.err.println("BGM doesn't exist!");
    
    ClassLoader cl = AsteroidGame.class.getClassLoader();
    URL resourceURL = cl.getResource("AsteroidGame/Audio/BGM/A Serious Mission.wav");
    if(resourceURL != null){
      AudioClip sound = soundMaster.newAudioClip(resourceURL);
      //sound.loop();
      LoopBGM loopBGM = new LoopBGM(sound,bgmFile);
      loopBGM.setDaemon(true);
      loopBGM.start();
    }
    
    mainFrame.setVisible(true);
    
  }
  
  public static class LoopBGM extends Thread{
    AudioClip bgmSound;
    Timer timer;
    AudioInputStream audioInputStream;
    AudioFormat format;
    long frames;
    double durationInSeconds;
    float startTime,nowTime;
    boolean playingSound;
    public LoopBGM(AudioClip sound, File bgmFile){
      bgmSound = sound;
      /*try{
        audioInputStream = AudioSystem.getAudioInputStream(bgmFile);
        format = audioInputStream.getFormat();
        frames = audioInputStream.getFrameLength();
        durationInSeconds = (frames+0.0)/format.getFrameRate();
      }catch(Exception e){
        System.err.println(e.getMessage());
        return;
      }*/
    }
    
    public void run(){
      //System.err.println("running");
      while(true){
        if(!playingSound){
          bgmSound.loop();
          playingSound = true;
          startTime = System.currentTimeMillis();
        }
        if(System.currentTimeMillis()-startTime == 97000){
          playingSound = false;
          bgmSound.stop();
          System.out.println("RESTART BGM");
        }
      }
      
      /* timer = new Timer(113,new ActionListener(){
        public void actionPerformed(ActionEvent evt){
          System.err.println("ActionPerformed");
          bgmSound.play();
        }
      });*/
    }
  }//End bgmloop thread
 
  
}
