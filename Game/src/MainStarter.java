import com.sun.org.apache.xpath.internal.SourceTree;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.TimerTask;

/**
 * Created by sn0wsky on 08.06.14.
 */
public class MainStarter {

    public static void main(String[] args) {
        Controller contr = new Controller();
        contr.initInterface();
        contr.showMenu();
    }


}
