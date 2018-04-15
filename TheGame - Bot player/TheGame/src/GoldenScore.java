import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class GoldenScore extends GameObject {
	
	public GoldenScore() {
		super((int) (Math.random() * (Game.WIDTH-64) + 1), (int) (Math.random() * (Game.HEIGHT-64) + 1), ID.GoldenScore, Color.GREEN); // creates basic enemy at random X and random Y
		velX = getNum((float) (Math.random() * 99) + 1); // get random value for velocity X;
		velY = getNum((float) (Math.random() * 99) + 1); // get random value for velocity Y;
	}

	public void tick() { // golden score updating method.
		x += velX;
		y += velY;
		
		bounceGoldenScore();
	}
	
	public void render(Graphics g) { // golden score graphics rendering method.
		g.setColor(Color.yellow);
		g.fillRect((int)x, (int)y, 16, 16);
	}
	
	public void bounceGoldenScore() { // // Screen bounds for the object.
		if (y <= 0 || y >= Game.HEIGHT - 50) {
			velY *= -1;
		}

		if (x <= 0 || x >= Game.WIDTH - 32) {
			velX *= -1;
		}
	}
	
	public float getNum(float num) { // return random value, 1 or -1 for the object.
		if(num >= 50) {
			return 1;
		} else {
			return -1;
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 16, 16);
	}
}
