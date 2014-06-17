import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by sn0wsky on 08.06.14.
 */
public class Ball {
    int x = 250;
    int y = 350;
    int angle;
    int speed = 2;
    int radius = 20;
    Color color;
    Desk desk1;
    DeskBot desk2;
    boolean changed = false;
    BufferedImage ballImg;
    int losed = 0;

    public Ball(Desk desk1){
        try {
            ballImg = ImageIO.read(new File("img/ball.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.desk1 = desk1;
        color = new Color(123,255,123);
        angle = (int) Math.round(Math.random()*90-45);
        speed = 5;
        x = 500/2;
        y = 700/2;
        //radius = (int) Math.round(Math.random()*30+1);
        //System.out.println(angle);

    }
    public void draw(Graphics2D imgGraph){
        imgGraph.setColor(color);
        imgGraph.drawImage(ballImg,x,y,null);

    }
    public void move(){
        //System.out.println(x+ " "+y+" "+angle);
        if(x<=0 || x>=480){
            angle = 360-angle;
        }
        //else if(y<=0){
        //    angle = 180-angle;
        //}
        angle = angle % 360;
        if (x>=desk1.getLeft()-radius/2 && x<=desk1.getRight()-radius/2 && y>=desk1.getTop()-radius/2){
            System.out.println(angle);
            this.y = desk1.getTop()-radius/2;
            if(desk1.getMoveInt()==1){
                if(angle>=0 && angle<=90){
                    speed -= 2;
                    angle = 180-angle;
                    angle += 15;
                }
                if(angle<=360 && angle>=270){
                    speed += 2;
                    angle = 180-angle;
                    angle +=15;
                }
            }
            if(desk1.getMoveInt()==2){
                if(angle>=0 && angle<=90){
                    speed += 2;
                    angle = 180-angle;
                    angle -=15;
                }
                if(angle<=360 && angle>=270){
                    speed -= 2;
                    angle = 180-angle;
                    angle -= 15;
                }
            }
            if(desk1.getMoveInt()==0){
                angle = 180-angle;
            }

        }
        else if (x>=desk2.getLeft()-radius/2 && x<=desk2.getRight()-radius/2 && y<=desk2.getTop()-radius/2){
            System.out.println(angle);
            if(desk2.getMoveInt()==1){
                if(angle>=0 && angle<=90){
                    speed -= 2;
                    angle -= 15;
                }
                if(angle<=360 && angle>=270){
                    speed += 2;
                    angle -=15;
                }
            }
            if(desk2.getMoveInt()==2){
                if(angle>=0 && angle<=90){
                    speed += 2;
                    angle +=15;
                }
                if(angle<=360 && angle>=270){
                    speed -= 2;
                    angle += 15;
                }
            }
            angle = 180-angle;
        }
        if(speed<=2){
            speed = 2;
        }
        if(speed>=6){
            speed = 6;
        }
        angle = angle % 360;
        if (angle < 0){
            angle = 360 + angle;
        }
        x+=(int) Math.round(Math.sin(Math.toRadians(angle))*((double)speed));
        y+=(int) Math.round(Math.cos(Math.toRadians(angle))*((double)speed));
        if(x<0){
            x=0;
        }
        else if(x>480){
            x=480;
        }

        if(y<desk2.getTop()-20){
            losed = 2;
        }
        if(y>desk1.getTop()+20){
            losed = 1;
        }

    }
    public int checkLose(){
        return losed;
    }
    public void dropLose(){
        losed = 0;
    }
    public int getX(){return x;}
    public int getY(){return y;}
    public void setBotDesk(DeskBot desk){
        this.desk2 = desk;
    }
}
