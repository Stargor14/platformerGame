import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class mug {
    private BufferedImage image;
    public int x,y;
    public double[] momentums=new double[2];
    public int[][] corners=new int[4][2];
    public int spriteX=16;
    public int spriteY=16;
    Map m;
    public boolean dead=false;

    public mug(int X,int Y,double momentumX, double momentumY, Map M){
        x=X;
        y=Y;
        momentums[0]=momentumX;
        momentums[1]=momentumY;
        m=M;
        loadImage("sprites/player/"+helpers.color+"/spit.png");
    }
    public void update(){
        if(dead==false){
            x+=momentums[0]*(5/1000.0);
            y+=momentums[1]*(5/1000.0);
            momentums[1]-=0.0;

            corners=helpers.makeCorners(x, y, spriteX, spriteY);
    
            for(enemy e:m.enemies){//every enemy, platform and coin
                if(helpers.checkInside(corners, e.corners)&&e.hp>0){
                    e.hit();
                    dead=true;//projectile is dead, not enemy
                    helpers.score+=50;
                }
            }
            for(Platform p:m.platforms){//every enemy, platform and coin
                if(helpers.checkInside(corners, p.corners)){ 
                    dead=true;
                }
            }
            if(x>helpers.size || x<0){dead=true;}
        }
    }
    private void loadImage(String path) {
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }
    public void draw(Graphics g, ImageObserver observer) {
        if(dead==false){g.drawImage(image,x,helpers.size-y,observer);}
    }
}
