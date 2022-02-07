import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class coin {
    private BufferedImage image;
    public int x,y;
    public int[][] corners;
    public boolean existing=true;
    public int spriteX=32;
    public int spriteY=32;
    int ticks=0;
    int currImage=0;

    public coin(int X, int Y){
        x=X;
        y=Y;
        corners=helpers.makeCorners(x,y,spriteX,spriteY);
        loadImage("sprites/coins/coin"+currImage+".png");
    }
    private void loadImage(String path) {
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }
    public void draw(Graphics g, ImageObserver observer) {
        if(existing==true){
            ticks++;
            if(ticks==20){
                currImage++;
                if(currImage==7){
                    currImage=0;
                }
                loadImage("sprites/coins/coin"+currImage+".png");
                ticks=0;
            }
            g.drawImage(image,x,helpers.size-y,observer);//helpers.size-posy to make bottom of screen 0
        }
    }
}
