import java.awt.Color;
import java.awt.Graphics;

public class basicEnemy extends GameObject {
	
	public basicEnemy() {
		super((int) (Math.random() * Game.WIDTH + 1), (int) (Math.random() * Game.HEIGHT + 1), ID.basicEnemy); // creates basic enemy at random X and random Y
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
	
	public float getNum(float num) { // return random value, 5 or -5 for the object.
		if(num >= 50) {
			return 5;
		} else {
			return -5;
		}
	}
}
