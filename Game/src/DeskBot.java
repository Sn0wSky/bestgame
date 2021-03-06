import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DeskBot  implements Bot  {
    int x = 190;
    int y = 50;
    int width = 120;
    int height = 20;
    Color color = new Color(255, 0, 0);
    int moveInt = 0;
    int speed = 4;
    BufferedImage deskImg;
    Ball ball;
    boolean userKick = false;

    public DeskBot(Ball ball) {
        try {
            deskImg = ImageIO.read(new File("img/desk.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.ball = ball;
    }

    public void draw(Graphics2D imgGraph) {
        if (ball.getX() <= this.x + (width / 2)) {
            this.setMoveInt(1);
        } else if (ball.getX() > this.x + (width / 2)) {
            this.setMoveInt(2);
        }
        this.move();
        imgGraph.setColor(color);
        imgGraph.drawImage(deskImg, x, y, null);
    }

    public void move() {
        if (this.getLeft() <= 0 && moveInt == 1) {
            this.setMoveInt(0);
            x = 1;
        } else if (this.getRight() >= 500 && moveInt == 2) {
            this.setMoveInt(0);
            x = 499 - width;
        }
        if (this.getMoveInt() == 1) {
            this.x -= speed;
        }
        if (this.getMoveInt() == 2) {
            this.x += speed;
        }
    }

    public int getMoveInt() {
        return moveInt;
    }

    public int getLeft() {
        return x;
    }

    public int getRight() {
        return x + width;
    }

    public int getTop() {
        return y + height;
    }

    public void setMoveInt(int num) {
        this.moveInt = num;
    }

    public void setUserKickTrue(){ this.userKick = true; }
}
