import javax.swing.*;
class Game {
    
    public static void main(String[] args)throws Exception{
        //do init stuff
        initScreen();
    }
    public static void initScreen() throws Exception{
        JFrame window = new JFrame("game");//creates window using JFrame
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//sets closing windows with x to end application
        playArea game=new playArea();
        window.add(game);
        window.addKeyListener(game);
        window.setVisible(true);
        window.setResizable(false);
        window.pack();
        window.setLocationRelativeTo(null);
        
    }
}
