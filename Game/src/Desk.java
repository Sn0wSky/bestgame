import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by sn0wsky on 09.06.14.
 */
public class Desk {
    int x = 190;
    int y = 660;
    int width = 120;
    int height = 20;
    Color color = new Color(255,0,0);
    int moveInt=0;
    BufferedImage deskImg;

    public Desk(){
        try {
            deskImg = ImageIO.read(new File("img/desk.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D imgGraph){
        if(moveInt == 1){
            move(true);
        }
        else if(moveInt==2){
            move(false);
        }
        imgGraph.setColor(color);
        imgGraph.drawImage(deskImg,x,y,null);
    }

    public void move(boolean isLeft){
        if(this.getLeft()<=0 && moveInt == 1){
            this.setMoveInt(0);
            x=1;
        }
        else if (this.getRight()>=500 && moveInt == 2){
            this.setMoveInt(0);
            x=499-width;
        }
        if(isLeft){
            x-=5;
        }
        else{
            x+=5;
        }
    }

    public int getMoveInt(){ return moveInt; }
    public int getLeft(){
        return x;
    }
    public int getRight(){
        return x+width;
    }
    public int getTop(){ return y; }
    public void setMoveInt(int num){
        this.moveInt=num;
    }
}
