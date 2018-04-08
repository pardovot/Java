import java.awt.Color;
import java.awt.Graphics;

public class FollowingEnemy extends GameObject {
	private Player player;

	public FollowingEnemy(Player player) {
		super((int) (Math.random() * Game.WIDTH + 1), (int) (Math.random() * Game.HEIGHT + 1), ID.FollowingEnemy); // creates basic enemy at random X and random Y
		this.player = player;
	}

	public void tick() { // following enemy updating method.
		x += velX;
		y += velY;

		followPlayer();

	}

	public void render(Graphics g) { // following enemy graphics rendering method.
		g.setColor(Color.blue);
		g.fillRect((int) x, (int) y, 16, 16);
	}

	public int getNum(int num) { // return random value, 5 or -5 for the object.
		if (num >= 50) {
			return 5;
		} else {
			return -5;
		}
	}

	public void followPlayer() { // calculates the directions between following enemy and the player, and adjusts the X and Y according to it.
		float diffX = x - player.getX() - 8;
		float diffY = y - player.getY() - 8;
		float distance = (float) Math.sqrt((x - player.getX()) * (x - player.getX()) + ((y - player.getY()) * (y - player.getY())));
		
		velX = ((-1 / distance) * diffX);
		velY = ((-1 / distance) * diffY);
	}
}
