import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DeskBotHard implements Bot {
    int x = 190;
    int y = 50;
    int width = 120;
    int height = 20;
    Color color = new Color(255, 0, 0);
    int moveInt = 0;
    int speed = 2;
    BufferedImage deskImg;
    Ball ball;
    Ball cleverBall;
    int futureOfX = 0;
    boolean userKick = false;
    boolean moveToBall = false;
    int middleX = 0;

    public DeskBotHard(Ball ball) {
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
        if (userKick) {
            userKick = false;
            cleverBall = ball.getCopy();
            while (cleverBall.getY() >= this.getTop()) {
                cleverBall.move();
            }
            futureOfX = cleverBall.getX();
            moveToBall = true;
        }

        if (moveToBall == true) {
            if (Math.abs(futureOfX - getMiddleX()) > speed) {
                if (futureOfX >= getMiddleX()) {
                    this.x += speed;
                } else {
                    this.x -= speed;
                }
            }
            else{
                moveToBall = false;
            }
        }
    }

    public int getMiddleX() {
        return (this.getLeft() + this.getRight()) / 2;
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

    public void setUserKickTrue() {
        this.userKick = true;
    }
}
