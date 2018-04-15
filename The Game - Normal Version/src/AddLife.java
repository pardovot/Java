import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class AddLife extends GameObject {

	public AddLife() {
		super((int) (Math.random() * (Game.WIDTH - 64) + 1), (int) (Math.random() * (Game.HEIGHT - 64) + 1), ID.AddLife,
				Color.GREEN); // creates life object at random X and random Y

		velX = getNum((float) (Math.random() * 99) + 1); // get random value for velocity X;
		velY = getNum((float) (Math.random() * 99) + 1); // get random value for velocity Y;
	}

	public void tick() { // AddLife's update method.
		x += velX;
		y += velY;

		bounceGoldenScore();
	}

	public void render(Graphics g) { // AddLife's graphics rendering method.
		g.setColor(Color.green);
		g.fillRect((int) x, (int) y, 16, 16);
	}

	public void bounceGoldenScore() { // Screen bounds for the object.
		if (y <= 0 || y >= Game.HEIGHT - 50) {
			velY *= -1;
		}

		if (x <= 0 || x >= Game.WIDTH - 32) {
			velX *= -1;
		}
	}

	public float getNum(float num) { // return random value, 1 or -1 for the object.
		if (num >= 50) {
			return 1;
		} else {
			return -1;
		}
	}

	public Rectangle getBounds() { // returns a rectangle of the life object.
		return new Rectangle((int) x, (int) y, 16, 16);
	}

}