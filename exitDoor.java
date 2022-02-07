import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class exitDoor {
    private BufferedImage image;
    public int posx,posy;
    public int[][] corners;
    public boolean closed=true;

    public exitDoor(int X, int Y){
        posx=X;
        posy=Y;
        corners=helpers.makeCorners(posx, posy, 96, 96);
        loadImage();
    }

    private void loadImage() {
        try {
            if(closed){
                image = ImageIO.read(new File("sprites/doorclosed.png"));
            }
            else{
                image = ImageIO.read(new File("sprites/dooropen.png"));
            }
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }
    public void draw(Graphics g, ImageObserver observer) {
        g.drawImage(image,posx,helpers.size-posy,observer);//helpers.size-posy to make bottom of screen 0
    }
    public void open(){
        closed=false;
        loadImage();
    }
}
