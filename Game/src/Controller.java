import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.TimerTask;

/**
 * Created by sn0wsky on 16.06.14.
 */
public class Controller {
    public static JFrame frame;
    public static JPanel table;
    int fps = 0;
    int maxfps = 0;
    long now;
    Desk desk;
    DeskBot bot;
    java.util.Timer timer;
    boolean isPaused = false;
    boolean firstStart = true;

    BufferedImage temp;

    boolean startChoosed = true;
    BufferedImage menuStart = null;
    BufferedImage menuExit = null;

    int pauseChoosed = 0;
    BufferedImage pauseResume = null;
    BufferedImage pauseNewGame = null;
    BufferedImage pauseExit = null;

    String score = "You 0 : 0 Bot";
    int scoreYou = 0;
    int scoreBot = 0;

    public void showMenu() {
        try {
            menuStart = ImageIO.read(new File("img/menu_start.jpg"));
            menuExit = ImageIO.read(new File("img/menu_exit.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        startChoosed = true;
        Graphics2D g2 = (Graphics2D) table.getGraphics();
        g2.drawImage(menuStart, 0, 0, null);
        table.removeKeyListener(gameKeyAdapter);
        table.addKeyListener(menuKeyAdapter);
    }

    public void showPause(){
        try {
            pauseResume = ImageIO.read(new File("img/pause_resume.png"));
            pauseNewGame = ImageIO.read(new File("img/pause_new_game.png"));
            pauseExit = ImageIO.read(new File("img/pause_exit.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pauseChoosed = 0;
        Graphics2D g2 = (Graphics2D) table.getGraphics();
        g2.drawImage(pauseResume, 0, 0, null);
        table.removeKeyListener(gameKeyAdapter);
        table.addKeyListener(pauseKeyAdapter);

    }

    public void newGame() {
        firstStart=true;
        desk = new Desk();
        final Ball ball = new Ball(desk);
        bot = new DeskBot(ball);
        ball.setBotDesk(bot);
        //now = System.currentTimeMillis();
        timer = new java.util.Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                if (!isPaused) {

                    temp = new BufferedImage(500, 700, BufferedImage.TYPE_INT_RGB);
                    Graphics2D tempGraphics = (Graphics2D) temp.getGraphics();
                    ball.move();
                    ball.draw(tempGraphics);
                    desk.draw(tempGraphics);
                    bot.draw(tempGraphics);
                    //incfps();
                    tempGraphics.setFont(new Font("Arial", Font.BOLD, 30));
                    tempGraphics.setColor(Color.white);
                    tempGraphics.drawString("You "+scoreYou+" : "+scoreBot+" Bot", 150, 30);
                    if(ball.checkLose()==1){
                        scoreBot+=1;
                        timer.cancel();
                        newGame();
                    }
                    else if(ball.checkLose()==2){
                        scoreYou+=1;
                        timer.cancel();
                        newGame();
                    }
                    //if (-now + System.currentTimeMillis() > 1000) {
                    //    maxfps = fps;
                    //    fps = 0;
                    //    now = System.currentTimeMillis();
                    //}
                    table.getGraphics().drawImage(temp, 0, 0, null);
                    if(firstStart){
                        try {
                            Thread.sleep(300);
                            firstStart=false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        timer.schedule(task, 0, 10);
    }

    public void incfps() {
        fps += 1;
    }

    boolean leftPressed = false;
    boolean rightPressed = false;

    public void initInterface() {
        frame = new JFrame();
        frame.setResizable(false);
        frame.setSize(new Dimension(500, 730));
        JPanel mainPanel = new JPanel();
        mainPanel.setSize(new Dimension(500, 730));
        mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        table = new JPanel();
        table.setSize(new Dimension(500, 700));
        table.setPreferredSize(new Dimension(500, 700));
        table.setBackground(Color.black);
        table.setFocusable(true);
        //table.removeKeyListener(menuKeyAdapter);
        //table.addKeyListener(gameKeyAdapter);

        mainPanel.add(table);
        frame.add(mainPanel);
        frame.show();

    }

// Key Adapters \/ \/ \/ ---------------------------------------------------------------------------------

// ---------------------Main Menu Adapter---------------------------

    KeyAdapter menuKeyAdapter = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (!startChoosed) {
                    Graphics2D g2 = (Graphics2D) table.getGraphics();
                    g2.drawImage(menuStart, 0, 0, null);
                    startChoosed = true;
                } else if (startChoosed) {
                    Graphics2D g2 = (Graphics2D) table.getGraphics();
                    g2.drawImage(menuExit, 0, 0, null);
                    startChoosed = false;
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (startChoosed) {
                    table.removeKeyListener(menuKeyAdapter);
                    table.addKeyListener(gameKeyAdapter);
                    newGame();
                } else {
                    System.exit(0);
                }
            }
        }
    };

// ----------------------Pause Adapter--------------------------
    KeyAdapter pauseKeyAdapter = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_UP){
                pauseChoosed -= 1;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN){
                pauseChoosed += 1;
            }
            if (pauseChoosed == -1){
                pauseChoosed = 2;
            }
            else if (pauseChoosed == 3){
                pauseChoosed = 0;
            }
            Graphics2D g2 = (Graphics2D) table.getGraphics();
            if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (pauseChoosed == 0) {
                    g2.drawImage(temp, 0, 0, null);
                    g2.drawImage(pauseResume, 0, 0, null);
                } else if (pauseChoosed == 1) {
                    g2.drawImage(temp, 0, 0, null);
                    g2.drawImage(pauseNewGame, 0, 0, null);
                } else if (pauseChoosed == 2) {
                    g2.drawImage(temp, 0, 0, null);
                    g2.drawImage(pauseExit, 0, 0, null);
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_ENTER){
                if (pauseChoosed == 0){
                    isPaused = false;
                    table.removeKeyListener(pauseKeyAdapter);
                    table.addKeyListener(gameKeyAdapter);
                }
                else if (pauseChoosed == 1){
                    isPaused = false;
                    table.removeKeyListener(pauseKeyAdapter);
                    table.addKeyListener(gameKeyAdapter);
                    timer.cancel();
                    newGame();
                }
                else if (pauseChoosed == 2){
                    System.exit(0);
                }
            }
        }
    };
// ----------------------Game Adapter---------------------------

    KeyAdapter gameKeyAdapter = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                desk.setMoveInt(1);
                leftPressed = true;
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                desk.setMoveInt(2);
                rightPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (!isPaused){
                    isPaused = true;
                    showPause();
                }
            }
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            }
            if (!leftPressed && !rightPressed) {
                desk.setMoveInt(0);
            }
        }

    };



}
