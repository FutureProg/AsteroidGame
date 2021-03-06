/**
 * The score is generated in the following
 * format:
 * name
 * score
 * 
 */
package AsteroidGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class HighScoreTable extends JPanel{
  int x,y;
  ArrayList<Score> highScore;
  File scoreFile;
  Scanner scoreRead;
  PrintStream fileOutput;
  int gameScore,gameHits,finalScore;
  JLabel yourScore,yourHits,yourTotal;
  JLabel[] scoreList;
  JTextField txtName;
  String yourName;
  boolean canEnter;
  
  public HighScoreTable(int score, int hits) throws IOException{ 
    setLayout(new GridLayout(18,1));
    
    highScore = new ArrayList<Score>();
    scoreFile = new File("AsteroidGame/HighScores.data");
    canEnter = true;
    
    if (!scoreFile.exists()){
        scoreFile.createNewFile();
        fileOutput = new PrintStream(scoreFile);
        fileOutput.print("");
    }//creates file if it doesn't exist.*/
    
    scoreRead = new Scanner(scoreFile);
    System.out.println("Highscores");
    setBackground(Color.WHITE);
    this.gameScore = score;
    this.gameHits = hits;
    finalScore = gameScore+gameHits;
    System.out.println("Your Score: " + this.gameScore);
    
    //create the score label.
    yourTotal = new JLabel("Your Score: " +finalScore,JLabel.CENTER);
    yourTotal.setForeground(Color.RED);
    yourTotal.setFont(new Font("Arial",Font.BOLD,20));
    add(yourTotal);
    yourScore = new JLabel("Your Time: " +gameScore,JLabel.CENTER);
    yourScore.setForeground(Color.BLACK);
    yourScore.setFont(new Font("Arial",Font.BOLD,16));
    add(yourScore);
    yourHits = new JLabel("Hit Bonus: " + gameHits,JLabel.CENTER);
    yourHits.setForeground(Color.BLACK);
    yourHits.setFont(new Font("Arial",Font.BOLD,16));
    add(yourHits);
    
    //create the text field.
    add(new JLabel("Your name:", JLabel.CENTER));
    txtName = new JTextField("",3);
    txtName.setHorizontalAlignment(JTextField.CENTER);
    txtName.addKeyListener(new KeyAdapter(){
      public void keyPressed(KeyEvent evt){
        int code = evt.getKeyCode();
        if (code == KeyEvent.VK_ENTER&&txtName.getText().trim()!=""&&canEnter){
          yourName = txtName.getText().trim();
          System.out.println(yourName);
          highScore.add(new Score(finalScore,yourName));
          printToFile();
          txtName.setEditable(false);
          
          //create the button
          JButton quit = new JButton("QUIT");
          quit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
              if (yourName != null)
                System.exit(0);
              else
                JOptionPane.showMessageDialog(new JFrame("Message"),"You haven't entered your name");
          }
        });
          add(quit);
          canEnter = false;      
        }
        
        
      }
    });
    add(txtName);
    
    readFromFile();
    
    Score[] gotScores = new Score[10];
    scoreList = new JLabel[10];
    for (int i = 0; i<highScore.size()&&i!=10;i++){
      scoreList[i] = new JLabel(""+highScore.get(i).name+"          "+highScore.get(i).value,JLabel.CENTER);
      add(scoreList[i]);
    }
    
    
    
  }
  
  
  public class Score{
    int value;
    String name;
    
    public Score(int value, String name){
      this.value = value;
      this.name = name;
    }
  }
  
  /*prints to the HighScore.txt file*/
  public void printToFile(){
    arrangeScores();
    try{
    fileOutput = new PrintStream(scoreFile);
    for(int i=0;i<highScore.size();i++){
      fileOutput.println(highScore.get(i).name);
      fileOutput.println(highScore.get(i).value);
    }
    }catch(IOException e){}
    for (int i = 0; i<highScore.size()&&i!=10;i++){
      if(scoreList[i] != null){
        remove(scoreList[i]);
        scoreList[i] = new JLabel(""+highScore.get(i).name+"          "+highScore.get(i).value,JLabel.CENTER);
        add(scoreList[i]);
      }
      System.out.println("PrintToFile " +i);
      revalidate();
    }
  }
  
  /*reads from HighScore.txt file*/
  public void readFromFile(){
    int newScore = 0;
    String newName = "";
    for(int i=0;scoreRead.hasNextLine();i++){
      if (scoreRead.hasNextLine()){
        newName = scoreRead.nextLine();
       // System.out.println("GOt LINE");
      }
      
      //System.out.println("Getting score");
      
      if (scoreRead.hasNextInt()){
        newScore = scoreRead.nextInt();
        //System.out.println("GOT INT");
      }
      if (newScore != -1 && newName != ""){
        highScore.add(new Score(newScore,newName));
        //System.out.println(highScore.get(i).name);
        //System.out.println(highScore.get(i).value);
      }
      newScore = -1;
      newName = "";
    }
  }
  
  public void arrangeScores(){
    int maxLoc= highScore.size()-1;
    int startArea = highScore.size()-1;
    
    for (int i = highScore.size()-1;i>=0;i--){
      if (highScore.get(startArea).value > highScore.get(i).value){
        maxLoc--;
      }
    }
    Score temp = new Score(highScore.get(maxLoc).value,highScore.get(maxLoc).name);
    highScore.set(maxLoc,new Score(highScore.get(startArea).value,highScore.get(startArea).name));
    if(maxLoc == highScore.size()-1)
      highScore.set(maxLoc,temp);
    else
      highScore.set(maxLoc+1,temp);
  }
}
