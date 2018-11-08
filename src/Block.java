import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

class Block extends Rectangle {

	private Image img;
	int dx = 3;
	int dy = -3;
	Rectangle left, right;
	private boolean destroyed = false;
	boolean powerUp = false;

	Block(int a, int b, int w, int h, String s) {
		x = a;
		y = b;
		width = w;
		height = h;
		left = new Rectangle(a - 1, b, 1, h);
		right = new Rectangle(a + w + 1, b, 1, h);
		img = Toolkit.getDefaultToolkit().getImage(s);
	}
	
	void draw(Graphics g, Component c) {
		if (!destroyed) {
            g.drawImage(img, x, y, width, height, c);
        }
	}

    public boolean isDestroyed() {
        return destroyed;
    }

    public void destroy() {
        this.destroyed = true;
    }
}
