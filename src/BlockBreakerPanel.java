import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.stream.IntStream;

import javax.swing.JPanel;

import static java.util.Map.entry;

public class BlockBreakerPanel extends JPanel implements KeyListener {

    private static final Map<String, Integer> COLOR_AND_PLACEMENT = Map.ofEntries(
            entry("blue", 0),
            entry("green", 25),
            entry("lime", 50),
            entry("brown", 75),
            entry("red", 100)
    );

    private ArrayList<Block> blocks = new ArrayList<>();
    private ArrayList<Block> balls = new ArrayList<>();
    private ArrayList<Block> powerUps = new ArrayList<>();
    private Block paddle;


    BlockBreakerPanel() {
        createPaddle();
        createBall(237);
        createBlocks();
        createPowerUps();

        addKeyListener(this);
        setFocusable(true);
    }

    private void createPaddle() {
        paddle = new Block(175, 480, 150, 25, "paddle.png");
    }

    private void createPowerUps() {
        Random random = new Random();
        IntStream.range(0, 7).forEach((v) -> blocks.get(random.nextInt(32)).powerUp = true);
    }

    private void createBlocks() {
        IntStream.range(0, 8).forEach(column -> {
            COLOR_AND_PLACEMENT.forEach((color, row) -> {
                blocks.add(new Block((column * 60 + 2), row, 60, 25, color + ".png"));
            });
        });
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        blocks.forEach(block -> block.draw(g, this));
        balls.forEach(block -> block.draw(g, this));
        powerUps.forEach(block -> block.draw(g, this));

        paddle.draw(g, this);
    }

    void update() {
        for (Block powerUp : powerUps) {
            powerUp.y += 1;
            if (powerUp.intersects(paddle) && !powerUp.isDestroyed()) {
                powerUp.destroy();
                createBall(paddle.dx + 75);
            }
        }
        for (Block ball : balls) {
            int size = 25;
            ball.x += ball.dx;

            if (ball.x > (getWidth() - size) && ball.dx > 0 || ball.x < 0) {
                ball.dx *= -1;
            }

            if (ball.y < 0 || ball.intersects(paddle)) {
                ball.dy *= -1;
            }

            ball.y += ball.dy;

            for (Block block : blocks) {
                if ((block.left.intersects(ball) || block.right.intersects(ball)) && !block.isDestroyed()) {
                    ball.dx *= -1;
                    popBlock(block);
                } else if (ball.intersects(block) && !block.isDestroyed()) {
                    ball.dy *= -1;
                    popBlock(block);
                }
            }
        }
        repaint();
    }

    private void popBlock(Block block) {
        block.destroy();
        if (block.powerUp) {
            popPowerUp(block);
        }
    }

    private void createBall(int x) {
        balls.add(new Block(x, 437, 30, 30, "ball.png"));
    }

    private void popPowerUp(Block block) {
        powerUps.add(new Block(block.x, block.y, 30, 30, "extra.png"));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            Animate animate = new Animate(this);
            Thread thread = new Thread(animate);
            thread.start();
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT && paddle.x > 0) {
            paddle.x -= 15;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && paddle.x < (getWidth() - paddle.width)) {
            paddle.x += 15;
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {}

    @Override
    public void keyTyped(KeyEvent arg0) {}
}
