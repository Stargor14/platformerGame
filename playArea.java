import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;
import java.io.File;
import java.util.Random;
import java.text.DecimalFormat;

public class playArea extends JPanel implements ActionListener, KeyListener{
    player_ player;
    enemy enemy;
    private Timer timer;
    Map currMap;
    public mug[] mugs;
    int delay=5;//ms delay between each frame
    JLabel scoreLabel = new JLabel();//score label object, global so that we can rewrite the score every tick
    JLabel title = new JLabel();
    JLabel hiscore = new JLabel();
    JLabel defeatTitle = new JLabel();
    JLabel defeatScore = new JLabel();
    JLabel timeLabel = new JLabel();
    JLabel lifeLabel = new JLabel();
    int ticks=0;
    int totalticks=0;
    boolean movingLeft=false;
    boolean movingRight=false;
    int gameState=0;
    int level=1;
    Font arcade=createFont();
    JButton green,blue,red,black,color,playAgain,quitButton;
    String highscore;
    DecimalFormat df=new DecimalFormat("0.000");
    boolean lossadded=false;

    public playArea() throws Exception{
        setPreferredSize(new Dimension(helpers.size,helpers.size));
        setBackground(new Color(1, 1, 1));
        //startLevel(helpers.level);
        timer = new Timer(delay, this);
        timer.start();
        addStart();
    }
    public void startLevel(int l) throws Exception{
        ticks=0;
        currMap=new Map(l);
        player=new player_(currMap);
        addGame();
    }
    public static String randomColor() {
        String[] colors={"green","red","blue"};
        return colors[new Random().nextInt(3)];
    }
    public void commenceGame(){
        gameState=1;
        clearScreen();
        helpers.level=1;
        try{startLevel(helpers.level);}catch(Exception e){}
        try{helpers.stopTitle();}catch(Exception e){}
        try{helpers.stopDefeat();}catch(Exception e){}
        try{helpers.playBackground();}catch(Exception e){}
        lossadded=false;
        helpers.score=0;
    }
    @Override
    public void actionPerformed(ActionEvent e){
    /*  
        checkPlayerInput()
        checkInAir()
        applyInput()//move player, shoot pellet, make sure player not off map, etc
        function to check if player is out of bounds before redraw
        checkActions()//hit by enemy, on coin, on level exit
        applyActions() change hp, change score, go onto next level, etc
        redrawGraphics()
        gameState 0=menu, 1=game, 2=victory, 3=loss
    */  
    if(e.getSource()==green){helpers.color="green";commenceGame();}
    if(e.getSource()==blue){helpers.color="blue";commenceGame();}
    if(e.getSource()==red){helpers.color="red";commenceGame();}
    if(e.getSource()==quitButton){System.exit(0);}
    if(e.getSource()==black || e.getSource()==color){
        helpers.color=randomColor();
        commenceGame();
    }
    if(gameState==0){//if menu
        ticks++;
        if(ticks==333/delay){ticks=0;color.setVisible(!color.isVisible());black.setVisible(!color.isVisible());}//if visible, make invisble, if invisble, make visible
    }

    if(gameState==1){
        for(enemy en:currMap.enemies){
            en.update();
        }
        player.update(movingLeft||movingRight);
        scoreLabel.setText("score: "+ helpers.score);
        timeLabel.setText("Time: "+ df.format(totalticks*delay/1000.0));
        lifeLabel.setText("Lives: "+helpers.hp);
        ticks++;
        totalticks++;
        if(ticks==1000/delay){ticks=0;if(helpers.score>0){helpers.score-=5;}}
        if(helpers.level>level){
            level++;
            try{
                startLevel(level);
            }
            catch(Exception ex){
            }
        }
        if(helpers.lost==true){
            gameState=2;
        }
        if(helpers.won==true){
            gameState=3;
        }
        repaint();
    }
    if(gameState==2){//losing gamestate
        if(!lossadded){clearScreen();addLoss();lossadded=true;helpers.stopBackground();}
    } 
    if(gameState==3){
        if(!lossadded){clearScreen();addVictory();lossadded=true;helpers.stopBackground();}
    } 
}
    public void addStart(){
        Font titleFont = arcade.deriveFont(85f);
        Font scoreFont = arcade.deriveFont(50f);
        title.setText("Super Seow Bros!");
        title.setFont(titleFont);
        try{
            Scanner inp = new Scanner(new File("hiscore.txt"));
            highscore=inp.nextLine();
        }
        catch(Exception ex){highscore="0000000";}
        hiscore.setText("HIGHSCORE "+highscore);
        hiscore.setFont(scoreFont);
        Dimension titlesize = title.getPreferredSize();
        title.setBounds(100, 100, titlesize.width, titlesize.height);
        Dimension size = hiscore.getPreferredSize();
        hiscore.setBounds(15, 800, size.width, size.height);
        green=setButton(225,325,400,50,"sprites/startGreen.png");
        blue=setButton(225,400,400,50,"sprites/startBlue.png");
        red=setButton(225,475,400,50,"sprites/startRed.png");
        color=setButton(225,550,400,50,"sprites/startRandomColor.png");
        black=setButton(225,550,400,50,"sprites/startRandomBlack.png");
        setLayout(null);
        title.setForeground(new Color(255, 0, 0));
        hiscore.setForeground(new Color(255, 0, 0));
        add(title);
        add(hiscore);
        helpers.playTitle();
    }
    public JButton setButton(int x, int y, int width, int height, String path){
        JButton button=new JButton(new ImageIcon(path));    
        button.setBounds(x,y,width,height);    
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.addActionListener(this);
        button.setFocusable(false);
        add(button);
        return button;
    }
    public void addGame(){
        scoreLabel.setText("Score 00000000000");
        scoreLabel.setFont(arcade.deriveFont(45f));
        lifeLabel.setText("Lives: 3");
        lifeLabel.setFont(arcade.deriveFont(45f));
        scoreLabel.setText("Score 00000000000");
        Dimension size = scoreLabel.getPreferredSize();
        scoreLabel.setBounds(340, 40, size.width, size.height);
        timeLabel.setText("Time 00000000000");
        timeLabel.setFont(arcade.deriveFont(45f));
        timeLabel.setBounds(340, 80, size.width, size.height);
        lifeLabel.setBounds(640, 80, size.width, size.height);
        scoreLabel.setForeground(Color.white);
        timeLabel.setForeground(Color.white);
        lifeLabel.setForeground(Color.white);
        add(scoreLabel);
        add(timeLabel);
        add(lifeLabel);
    }
    public void addVictory(){
        Font titleFont = arcade.deriveFont(85f);
        Font scoreFont = arcade.deriveFont(50f);
        defeatTitle.setText("Victory!!!");
        defeatTitle.setFont(titleFont);
        defeatScore.setText("SCORE "+helpers.score);
        defeatScore.setFont(scoreFont);
        Dimension titlesize = defeatTitle.getPreferredSize();
        defeatTitle.setBounds(200,0, titlesize.width, titlesize.height);
        Dimension size = defeatScore.getPreferredSize();
        defeatScore.setBounds(350, 200, size.width, size.height);
        //playAgain=setButton(275,275,400,50,"sprites/playAgain.png");
        quitButton=setButton(225,400,400,50,"sprites/quit.png");
        setLayout(null);
        defeatTitle.setForeground(Color.white);
        defeatScore.setForeground(Color.white);
        add(defeatTitle);
        add(defeatScore);
        helpers.playDefeat();
    }
    public void addLoss(){
        Font titleFont = arcade.deriveFont(85f);
        Font scoreFont = arcade.deriveFont(50f);
        defeatTitle.setText("DEFEAT!!!");
        defeatTitle.setFont(titleFont);
        defeatScore.setText("SCORE "+helpers.score);
        defeatScore.setFont(scoreFont);
        Dimension titlesize = defeatTitle.getPreferredSize();
        defeatTitle.setBounds(200, 0, titlesize.width, titlesize.height);
        Dimension size = defeatScore.getPreferredSize();
        defeatScore.setBounds(350, 200, size.width, size.height);
        //playAgain=setButton(275,275,400,50,"sprites/playAgain.png");
        quitButton=setButton(225,400,400,50,"sprites/quit.png");
        setLayout(null);
        defeatTitle.setForeground(Color.white);
        defeatScore.setForeground(Color.white);
        add(defeatTitle);
        add(defeatScore);
        helpers.playDefeat();
    }
    public void clearScreen(){
        try{remove(green);}catch(Exception ex){}
        try{remove(blue);}catch(Exception ex){}
        try{remove(red);}catch(Exception ex){}
        try{remove(color);}catch(Exception ex){}
        try{remove(black);}catch(Exception ex){}
        try{remove(title);}catch(Exception ex){}
        try{remove(hiscore);}catch(Exception ex){}
        //try{remove(defeatTitle);}catch(Exception ex){}
        //try{remove(defeatScore);}catch(Exception ex){}
        try{remove(scoreLabel);}catch(Exception ex){}
        //try{remove(timeLabel);}catch(Exception ex){}
        //try{remove(defeatScore);}catch(Exception ex){}
        //try{remove(quitButton);}catch(Exception ex){}
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        player.move(e,player.inAir);
        if(e.getKeyChar()=='a'){movingLeft=true;}
        if(e.getKeyChar()=='d'){movingRight=true;}
    }
    @Override
    public void keyReleased(KeyEvent e) {
        player.stop(e,player.inAir);
        if(e.getKeyChar()=='a'){movingLeft=false;}
        if(e.getKeyChar()=='d'){movingRight=false;}
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //Draw Background
        if(gameState!=0){
            currMap.draw(g,this);
            for(int i=0;i<=helpers.numMugs;i++){player.mugs[i].draw(g, this);}
            for(coin c:currMap.coins){c.draw(g,this);}
            for(enemy enemy:currMap.enemies){enemy.draw(g,this);}
            player.draw(g,this);
        }
        Toolkit.getDefaultToolkit().sync();
    }
    public Font createFont(){
        try{
            Font font = Font.createFont(Font.TRUETYPE_FONT, getClass().getResource("sprites/ARCADECLASSIC.TTF").openStream());   
        GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        genv.registerFont(font);
        font = font.deriveFont(30f);
        return font;
        }
        catch(Exception e){
            return new Font("Arial",0,30);//returns arial font incase arcade font doesnt want to load
        }
    }
}
