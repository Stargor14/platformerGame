import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class player_ {
    private BufferedImage image;
    int posx,posy;
    boolean inAir,onPlatform;
    double[] momentums={0.0,0.0,0.0};
    double jump_strength=1500;
    double speed=400.0;
    int playerWidth=64;
    int playerHeight=64;
    int[][] corners = new int[4][2];
    boolean justTouched=false;
    int hp=3;
    Map m;
    boolean facingRight=true;
    mug[] mugs=new mug[200000];
    int currImage=0;
    int ticks;
    int cooldown;
    int immunityTime=0;
    

    public player_(Map M) {
        m=M;
        loadImage("sprites/player/"+helpers.color+"/player0.png");
        posx=0;
        posy=playerWidth;
    }
    private void loadImage(String path) {
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }
    public void draw(Graphics g, ImageObserver observer) {
        g.drawImage(image,posx,helpers.size-posy,observer);//helpers.size-posy to make bottom of screen 0
    }
    public void move(KeyEvent c,boolean inAir){
        //x,y momentums 
        if(c.getKeyChar()=='a'){moveLeft();loadImage("sprites/player/"+helpers.color+"/playerL"+currImage+".png");}//m[0]=leftward momentum
        if(c.getKeyChar()=='d'){moveRight();loadImage("sprites/player/"+helpers.color+"/player"+currImage+".png");}//m[1]=rightward momentum
        if(c.getKeyCode()==32 && (onPlatform==true||inAir==false)){Jump();onPlatform=false;}//m[2]=upward momentum
        if(c.getKeyChar()=='s'){spit();}//throw java cups
        //instansiate java cup object @ side that player is facing, add momentum, delete on hit 
    }
    public void stop(KeyEvent c,boolean inAir){
        //x,y momentums 
        currImage=0;
        if(c.getKeyChar()=='a'){stopLeft();}
        if(c.getKeyChar()=='d'){stopRight();}
    }
    public void update(boolean moving){//moving variable is used to check if player is moving for animation purposes
        hp=helpers.hp;
        if(hp<=0){
            helpers.lost=true;
        }
        if(immunityTime>0){immunityTime--;}
        if(moving&&onPlatform){ticks++;}
        if(ticks==10){
            ticks=0;
            currImage++;
            if(currImage==4){currImage=0;}
            if(facingRight){
                loadImage("sprites/player/"+helpers.color+"/player"+currImage+".png");
            }
            else{
                loadImage("sprites/player/"+helpers.color+"/playerL"+currImage+".png");
            }
        }
        cooldown--;
        posx+=(momentums[1]-momentums[0])*(5/1000.0);//momentum(pixels/second)*tickrateInFractionSeconds
        posy+=momentums[2]*(5/1000.0);

        corners=helpers.makeCorners(posx, posy, playerWidth, playerHeight);

        if(posx<0){posx=0;}
        if(posx>helpers.size-playerWidth){posx=helpers.size-playerWidth;}
        if(posy<playerHeight){posy=playerHeight;inAir=false;}
        else if(onPlatform==true){inAir=false;}
        else{inAir=true;}
        if(inAir==true){momentums[2]-=jump_strength/50;}
        
        for(int i=0;i<=helpers.numMugs;i++){
            mugs[i].update();
        }
        checkCollision();
    }
    public void moveLeft(){momentums[0]=400;momentums[1]=0;facingRight=false;}
    public void moveRight(){momentums[1]=400;momentums[0]=0;facingRight=true;}
    public void Jump(){posy+=10;momentums[2]=jump_strength;}
    public void stopLeft(){momentums[0]=0;}
    public void stopRight(){momentums[1]=0;}

    public void checkCollision(){//include floor as a platform, get the left and right side clipping to work, dont allow jumping thru bottom of platform
            for(int i=0;i<m.platformNum;i++){
                if(corners[0][1]>=m.platforms[i].y-6 && corners[0][1]<=m.platforms[i].y+6 && ((corners[0][0]>=m.platforms[i].left && corners[0][0]<=m.platforms[i].length+m.platforms[i].left)||(corners[1][0]>=m.platforms[i].left && corners[1][0]<=m.platforms[i].length+m.platforms[i].left))){
                    onPlatform=false;
                    momentums[2]=-1;
                    posy=m.platforms[i].y-7;
                    break;
                }
                if(corners[3][1]>=m.platforms[i].y-10 && corners[3][1]<=m.platforms[i].y+10){
                    onPlatform=false;
                    if((corners[0][0]>=m.platforms[i].left && corners[0][0]<=m.platforms[i].length+m.platforms[i].left)||(corners[1][0]>=m.platforms[i].left && corners[1][0]<=m.platforms[i].length+m.platforms[i].left)){
                        onPlatform=true;
                        momentums[2]=1;
                        posy=m.platforms[i].y+playerHeight;
                        break;
                    }
                }
            }
        for(int i=0;i<m.coinNum;i++){
            if(helpers.checkInside(m.coins[i].corners,corners)&& m.coins[i].existing){//if coin is inside player and it still exists
                m.coins[i].existing = false;
                helpers.score+=100;//when coin picked up, increase score by 100
                helpers.playAudio("audios/coin.wav", 0);
                m.remCoins--;
            }
        }
        for(enemy e: m.enemies){
            if(helpers.checkInside(e.corners,corners)&& e.hp>0){
                if(immunityTime<=0){
                    helpers.hp-=1;
                    helpers.playAudio("audios/hit.wav", 0);
                }
                immunityTime=80;
            }
        }
        if(m.remCoins<=0){
            m.exit.open();
        }
        if(helpers.checkInside(corners, m.exit.corners)&&m.exit.closed==false){
            helpers.level++;
            helpers.numMugs=-1;
            if(helpers.level>helpers.lastlevel){
                helpers.won=true;
            }
        }
    }
    public void spit(){
        //create new java mug with specific position in constructor parameters, velocity will be relative to speed of player, so make 
        //mug vlocity higher based on players speed as well
        //mug self destruct on collision with enemy or platform
        //mug throws are influenced by gravity, and they can be thrown up as well
        if(cooldown<=0){
            if(facingRight){loadImage("sprites/player/"+helpers.color+"/playerS.png");}
            if(!facingRight){loadImage("sprites/player/"+helpers.color+"/playerSL.png");}
            double throwStrengthY = 0.0;
            if(facingRight){mug _mug=new mug(posx+64,posy-16,600.0,throwStrengthY,m);helpers.numMugs++;mugs[helpers.numMugs]=_mug;} 
            if(!facingRight){mug _mug=new mug(posx-16,posy-16,-500.0,throwStrengthY,m);helpers.numMugs++;mugs[helpers.numMugs]=_mug;}
            if(helpers.score>0){helpers.score-=5;}
            helpers.playAudio("audios/spit.wav", 0);
            cooldown=100;
        }
    }
}
