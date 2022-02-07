import java.io.File;
  
import javax.sound.sampled.AudioInputStream;//audio libraries
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class helpers {
    public static int level=1;//0 is loading screen, n+1 is ending screen, use this to load new map file
    public static int score=0;
    public static int numMugs=-1;
    public static Clip background;
    public static Clip title;
    public static Clip defeat;
    public static int size=850;
    public static String color="green";
    public static int time=0;
    public static boolean lost=false;
    public static boolean won=false;
    public static int lastlevel=5;
    public static int hp=3;

    public static boolean checkInside(int[][] object1,int[][] object2){//object 1 should be the smaller object
        for(int i=0;i<4;i++){
            int cornerx1=object1[i][0];
            int cornery1=object1[i][1];
            if(cornerx1>=object2[0][0]&&cornerx1<=object2[1][0]&&cornery1<=object2[0][1]&&cornery1>=object2[2][1]){//checking if an of object1s corners are inside object 2
                return true;
            }
        }
        return false;
    }
    public static int[][] makeCorners(int x,int y,int X,int Y){//posx,posy,sizex,sizey
        int[][] corners=new int[4][2];
        corners[0][0]=x;
        corners[0][1]=y;
        corners[1][0]=x+X;
        corners[1][1]=y;
        corners[2][0]=x;
        corners[2][1]=y-Y;
        corners[3][0]=x+X;
        corners[3][1]=y-Y;
        return corners;
    }
    public static void playAudio(String filePath,int loops){
        AudioInputStream audioInputStream;
        try{//need try statment for AudioSystem object
        Clip clip;
        audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        if(loops>0){clip.loop(loops);}//-1 makes the audio loop forever
        clip.start();
        }
        catch(Exception ex){
        }
    }
    public static void playBackground(){
        AudioInputStream audioInputStream;
        try{//need try statment for AudioSystem object
        audioInputStream = AudioSystem.getAudioInputStream(new File("audios/background.wav").getAbsoluteFile());
        background = AudioSystem.getClip();
        background.open(audioInputStream);
        background.loop(-1);//-1 makes the audio loop forever
        background.start();
        }
        catch(Exception ex){
        }
    }
    public static void stopBackground(){
        background.stop();
        background=null;//deletes background clip so that it can be replayed if you choose to play the game again
    }
    public static void playTitle(){
        AudioInputStream audioInputStream;
        try{//need try statment for AudioSystem object
        audioInputStream = AudioSystem.getAudioInputStream(new File("audios/title.wav").getAbsoluteFile());
        title = AudioSystem.getClip();
        title.open(audioInputStream);
        title.loop(-1);//-1 makes the audio loop forever
        title.start();
        }
        catch(Exception ex){
        }
    }
    public static void stopTitle(){
        title.stop();
        title=null;//deletes background clip so that it can be replayed if you choose to play the game again
    }
    public static void playDefeat(){
        AudioInputStream audioInputStream;
        try{//need try statment for AudioSystem object
        audioInputStream = AudioSystem.getAudioInputStream(new File("audios/victory.wav").getAbsoluteFile());
        defeat = AudioSystem.getClip();
        defeat.open(audioInputStream);
        defeat.loop(-1);//-1 makes the audio loop forever
        defeat.start();
        }
        catch(Exception ex){
        }
    }
    public static void stopDefeat(){
        defeat.stop();
        defeat=null;//deletes background clip so that it can be replayed if you choose to play the game again
    }
}
