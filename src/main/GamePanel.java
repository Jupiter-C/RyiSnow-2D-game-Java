package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

public class GamePanel extends JPanel implements Runnable { // this class inherites JPanel class
    // screen settings
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3;

    public final int tileSize = originalTileSize * scale; // 48x48 tile // access from other pacakage public
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 pixels
    public final int screenHeight = tileSize * maxScreenRow; // 576 pixels

    // WORLD SETTING
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxScreenRow;

    // FPS (Frame Per Second)
    int FPS = 60;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread; // start and stop
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public Player player = new Player(this, keyH);
    public SuperObject obj[] = new SuperObject[10];

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // set the size of this class (JPanel)
        this.setBackground(Color.black);
        this.setDoubleBuffered(true); 
        // all the drawing from this component will be done in an offscreen painting buffer; in short, it improves rendering performance
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setObject();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start(); // automatically call run() method
    }

    //2D games keep running when starting game
    // @Override
    // sleep method
    /* public void run() {
        // game loop

        // long currentTime = System.nanoTime(); // 1,000,000,000 nanoseconds = 1 second
        // long currentTime2 = System.currentTimeMillis(); // 1,000 milliseconds = 1 second

        double drawInterval = 1000000000/FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while(gameThread != null) {

            // 1 UPDATE: update information such as character positions
            update();
            // 2 DRAW: draw he screen with the updated information
            repaint(); // call paintComponent method
            
            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime /= 1000000; // nanoseconds to milliseconds

                if (remainingTime < 0) remainingTime = 0;

                Thread.sleep((long)remainingTime);

                nextDrawTime += drawInterval;

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    } */

    // delta/accumulate method
    public void run() {

        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            
            lastTime = currentTime;

            if (delta > 1) {
                update();
                repaint();
                delta--;
            }
        }

    }

    // 1 UPDATE: update information such as character positions
    public void update() {
        player.update();
    }
    
    // 2 DRAW: draw he screen with the updated information
    public void paintComponent(Graphics g) { // graphics is pencil
        super.paintComponent(g); // type when create paintComponent method
        Graphics2D g2 = (Graphics2D) g; // convert graph change to 2d graph

        // TILE
        tileM.draw(g2); // make sure draw the background first

        // OBJECT
        for (int i = 0; i < obj.length; i++) {
            if (obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }

        // PLAYER
        player.draw(g2);

        g2.dispose();
    }
}
