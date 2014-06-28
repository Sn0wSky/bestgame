import java.awt.*;

/**
 * Created by sn0wsky on 28.06.14.
 */
public interface Bot {
    public void move();

    public void draw(Graphics2D imgGraph);

    public int getMoveInt();

    public int getLeft();

    public int getRight();

    public int getTop();

    public void setMoveInt(int num);

    public void setUserKickTrue();
}
