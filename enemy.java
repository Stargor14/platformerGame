import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class enemy {
    private BufferedImage image;
    int posx,posy;
    boolean inAir,onPlatform;
    double[] momentums={0.0,0.0,0.0};
    double jump_strength=1500;
    int playerWidth=48;
    int playerHeight=64;
    int[][] corners = new int[4][2];
    boolean justTouched=false;
    int hp=3;
    Map m;
    boolean facingRight=false;
    int sincehit=0;//ticks since enemnt got hit, so you can actually see the hit animation
    public int leftBound;
    public int rightBound;

    public enemy(Map M,int x, int y,int l, int r) {
        m=M;
        posx=x;
        posy=y+playerHeight; 
        leftBound=l;
        rightBound=r;
        System.out.println(leftBound);
        System.out.println(rightBound);
        moveRight();
    }
    private void loadImage(String path) {
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }
    public void draw(Graphics g, ImageObserver observer) {  
        if(hp>0){
            g.drawImage(image,posx,helpers.size-posy,observer);//helpers.size-posy to make bottom of screen 
        }
        else{
            g.drawImage(image,posx,helpers.size-posy+18,observer);
        }
    }
    public void hit(){
        if(facingRight){
            loadImage("sprites/enemy/enemyHitR.png");
        }
        else{
            loadImage("sprites/enemy/enemyHitL.png");
        }
        hp--;
        sincehit=0;
    }
    public void update(){
        if(sincehit>=60){//60 tick delay to make hit animation visible
            if(facingRight){
                loadImage("sprites/enemy/enemyR.png");
            }
            else{
                loadImage("sprites/enemy/enemyL.png");
            }
        }
        else{
            sincehit++;
        }
        if(hp>0){
            if(sincehit>=60){
                posx+=(momentums[1]-momentums[0])*(5/1000.0);//momentum(pixels/second)*tickrateInFractionSeconds
                posy+=momentums[2]*(5/1000.0);
            }
            corners=helpers.makeCorners(posx, posy, playerWidth, playerHeight);
    
            if(posx<=leftBound){posx=leftBound;moveRight();}
            if(posx>=rightBound-playerWidth){posx=rightBound-playerWidth;moveLeft();}
            if(posy<playerHeight){posy=playerHeight;inAir=false;}
            else if(onPlatform==true){inAir=false;}
            else{inAir=true;}
            if(inAir==true){momentums[2]-=jump_strength/50;}
        }
        else{
            loadImage("sprites/enemy/enemyDead.png");
        }
        checkCollision();
    }
    public void moveLeft(){momentums[0]=200;momentums[1]=0;facingRight=false;loadImage("sprites/enemy/enemyL.png");}
    public void moveRight(){momentums[1]=200;momentums[0]=0;facingRight=true;loadImage("sprites/enemy/enemyR.png");}
    public void Jump(){momentums[2]=jump_strength;}
    public void stopMoving(){momentums[1]=0;momentums[0]=0;}
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
    }
}