import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class basicEnemy extends GameObject {
	
	public basicEnemy() {
		super((int) (Math.random() * (Game.WIDTH-64) + 1), (int) (Math.random() * (Game.HEIGHT-64) + 1), ID.basicEnemy); // creates
		velX = 5;
		velY = 5;
	}

	public void tick() { // basic enemy updating method.
		x += velX;
		y += velY;

		BounceEnemy();

	}

	public void render(Graphics g) { // basic enemy graphics rendering method.
		g.setColor(Color.red);
		g.fillRect((int) x, (int) y, 16, 16);
	}

	public void BounceEnemy() { // Screen bounds for the object.
		if (y <= 0 || y >= Game.HEIGHT - 50) {
			velY *= -1;
		}

		if (x <= 0 || x >= Game.WIDTH - 32) {
			velX *= -1;
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 16, 16);
	}
}
