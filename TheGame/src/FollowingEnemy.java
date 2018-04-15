import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class FollowingEnemy extends GameObject {
	private Player player;

	public FollowingEnemy(Player player) {
		super((int) (Math.random() * (Game.WIDTH-64) + 1), (int) (Math.random() * (Game.HEIGHT-64) + 1), ID.FollowingEnemy); // creates basic enemy at random X and random Y
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

	public void followPlayer() { // calculates the directions between following enemy and the player, and adjusts the X and Y according to it.
		float diffX = x - player.getX() - 8;
		float diffY = y - player.getY() - 8;
		float distance = (float) Math.sqrt((x - player.getX()) * (x - player.getX()) + ((y - player.getY()) * (y - player.getY())));
		
		velX = ((-1 / distance) * diffX);
		velY = ((-1 / distance) * diffY);
	}

	public Rectangle getBounds(){
        return new Rectangle((int) x,(int) y, 16, 16);
}
}
