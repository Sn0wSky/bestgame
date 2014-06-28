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

public class Controller {
    public static JFrame frame;
    public static JPanel table;
    int fps = 0;
    int maxfps = 0;
    long now;
    Desk desk;
    Bot bot;
    java.util.Timer timer;
    boolean isPaused = false;
    boolean firstStart = true;

    BufferedImage temp;

    boolean startChoosed = true;
    BufferedImage menuStart = null;
    BufferedImage menuExit = null;

    int pauseStage = 0;

    int pauseChoosed = 0;
    BufferedImage pauseResume = null;
    BufferedImage pauseNewGame = null;
    BufferedImage pauseExit = null;

    int botChoosed = 0;
    BufferedImage difEasy = null;
    BufferedImage difHard = null;

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

    public void showPause() {
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

    public void showDifficultyChooser() {
        try {
            difEasy = ImageIO.read(new File("img/difficulty_easy.png"));
            difHard = ImageIO.read(new File("img/difficulty_hard.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Graphics2D g2 = (Graphics2D) table.getGraphics();
        g2.drawImage(temp, 0, 0, null);
        g2.drawImage(difEasy, 0, 0, null);
    }

    public void newGame() {
        firstStart = true;
        desk = new Desk();
        final Ball ball = new Ball(desk);
        if (botChoosed == 0) {
            bot = new DeskBot(ball);
        } else {
            bot = new DeskBotHard(ball);
        }
        ball.setBotDesk(bot);
        BufferedImage tempReadBg = null;
        try {
            tempReadBg = ImageIO.read(new File("img/game_bg.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        final BufferedImage bg = tempReadBg;
        timer = new java.util.Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                if (!isPaused) {
                    temp = new BufferedImage(500, 700, BufferedImage.TYPE_INT_RGB);
                    Graphics2D tempGraphics = (Graphics2D) temp.getGraphics();
                    tempGraphics.drawImage(bg, 0, 0, null);
                    ball.move();
                    ball.draw(tempGraphics);
                    desk.draw(tempGraphics);
                    bot.draw(tempGraphics);
                    tempGraphics.setFont(new Font("Arial", Font.BOLD, 30));
                    tempGraphics.setColor(Color.white);
                    tempGraphics.drawString("You " + scoreYou + " : " + scoreBot + " Bot", 150, 30);
                    if (ball.checkLose() == 1) {
                        scoreBot += 1;
                        timer.cancel();
                        newGame();
                    } else if (ball.checkLose() == 2) {
                        scoreYou += 1;
                        timer.cancel();
                        newGame();
                    }
                    table.getGraphics().drawImage(temp, 0, 0, null);
                    if (firstStart) {
                        try {
                            Thread.sleep(300);
                            firstStart = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };
        timer.schedule(task, 0, 10);
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
                    //table.addKeyListener(gameKeyAdapter);
                    newGame();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    isPaused = true;
                    pauseStage = 1;
                    table.addKeyListener(pauseKeyAdapter);
                    showDifficultyChooser();
                } else {
                    System.exit(0);
                }
            }
        }
    };

    // ----------------------Pause Adapter--------------------------
    KeyAdapter pauseKeyAdapter = new KeyAdapter() {
        public void keyPressed(KeyEvent e) {
            if (pauseStage == 0) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    pauseChoosed -= 1;
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    pauseChoosed += 1;
                }
                if (pauseChoosed == -1) {
                    pauseChoosed = 2;
                } else if (pauseChoosed == 3) {
                    pauseChoosed = 0;
                }
                Graphics2D g2 = (Graphics2D) table.getGraphics();
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if (pauseChoosed == 0) {
                        g2.drawImage(temp, 0, 0, null);
                        g2.drawImage(pauseResume, 0, 0, null);
                    } else if (pauseChoosed == 1) {
                        scoreYou = 0;
                        scoreBot = 0;
                        g2.drawImage(temp, 0, 0, null);
                        g2.drawImage(pauseNewGame, 0, 0, null);
                    } else if (pauseChoosed == 2) {
                        g2.drawImage(temp, 0, 0, null);
                        g2.drawImage(pauseExit, 0, 0, null);
                    }
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (pauseChoosed == 0) {
                        isPaused = false;
                        table.removeKeyListener(pauseKeyAdapter);
                        table.addKeyListener(gameKeyAdapter);
                    } else if (pauseChoosed == 1) {
                        pauseStage = 1;
                        showDifficultyChooser();
                        //isPaused = false;
                        //table.removeKeyListener(pauseKeyAdapter);
                        //table.addKeyListener(gameKeyAdapter);
                        //timer.cancel();
                        //newGame();
                    } else if (pauseChoosed == 2) {
                        System.exit(0);
                    }
                }
            }
            else {
                Graphics2D g2 = (Graphics2D) table.getGraphics();
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN){
                    if(botChoosed==0){
                        //System.out.println(0);
                        g2.drawImage(temp, 0, 0, null);
                        g2.drawImage(difHard, 0, 0, null);
                        botChoosed=1;
                    }
                    else{
                        g2.drawImage(temp, 0, 0, null);
                        g2.drawImage(difEasy, 0, 0, null);
                        botChoosed=0;
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    pauseStage = 0;
                    isPaused=false;
                    table.removeKeyListener(pauseKeyAdapter);
                    table.addKeyListener(gameKeyAdapter);
                    timer.cancel();
                    newGame();

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
                if (!isPaused) {
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
