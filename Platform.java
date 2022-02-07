public class Platform {
    int left;
    int length;
    int y;
    int[][] corners;
    
    public Platform(int Left,int Length,int Y){
        left=Left;
        length=Length;
        y=Y;
        corners=helpers.makeCorners(left, y, length, 5);
    }
}
