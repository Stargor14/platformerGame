import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Scanner;

public class Map {
    private BufferedImage image;

    int platformNum;
    int coinNum;
    int enemyNum;
    int remCoins;
    boolean first=true;

    Platform[] platforms;
    coin[] coins;
    enemy[] enemies;

    exitDoor exit;
    
    public Map(int level) throws Exception{
        Scanner inp = new Scanner(new File("level"+level+".txt"));
        inp.useDelimiter(",");
        String[] tem=inp.nextLine().split(",",0);

        platformNum=Integer.parseInt(tem[0])+1;
        coinNum=Integer.parseInt(tem[1]);
        enemyNum=Integer.parseInt(tem[2]);

        platforms=new Platform[platformNum];
        coins=new coin[coinNum];
        enemies=new enemy[enemyNum];

        platforms[0]=new Platform(0,helpers.size,0);//floor platform
        for(int i=1;i<platformNum;i++){
            String[] temp= inp.nextLine().split(",",0);
            platforms[i]=new Platform(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]),Integer.parseInt(temp[2])*125);
        }
        for(int i=0;i<coinNum;i++){
            String[] temp= inp.nextLine().split(",",0);
            coins[i]=new coin(Integer.parseInt(temp[0]),Integer.parseInt(temp[1])+32);
        }
        for(int i=0;i<enemyNum;i++){
            String[] temp= inp.nextLine().split(",",0);
            enemies[i]=new enemy(this,Integer.parseInt(temp[0]),Integer.parseInt(temp[1]),Integer.parseInt(temp[2]),Integer.parseInt(temp[3]));
        }
        String[] temp= inp.nextLine().split(",",0);
        exit = new exitDoor(Integer.parseInt(temp[0]), Integer.parseInt(temp[1])+96);
        remCoins=coinNum;
        loadImage("sprites/platformL.png");
    }
    private void loadImage(String path) {
        try {
            image = ImageIO.read(new File(path));
        } catch (IOException exc) {
            System.out.println("Error opening image file: " + exc.getMessage());
        }
    }
    public void draw(Graphics g, ImageObserver observer) {
        //if(first){g.drawImage(backImage,0,0,observer);}
        for(int i=0;i<platformNum;i++){
            int x=16;
            loadImage("sprites/platformL.png");
            g.drawImage(image,platforms[i].left,helpers.size-platforms[i].y,observer);
            loadImage("sprites/platform.png");
            for(;x<platforms[i].length;x+=16){
                g.drawImage(image,x+platforms[i].left,helpers.size-platforms[i].y,observer);
            }
            loadImage("sprites/platformR.png");
            g.drawImage(image,x-16+platforms[i].left,helpers.size-platforms[i].y,observer);
        }
        exit.draw(g,observer);
    }
}
