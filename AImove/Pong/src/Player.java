import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Player extends GameObject {

	
	public Player() {
		super(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, ID.Player, 1); // initialize player in the center of the screen.
	}

	public void tick() { // player updating method.
		x += velX;
		y += velY;
		
		x = Game.Bounds(x, 0, Game.WIDTH - 48); // Bounds the player to the screen.
		y = Game.Bounds(y, 0, Game.HEIGHT - 70); // Bounds the player to the screen.

	}

	public void render(Graphics g) { // player graphics rendering method.
		g.setColor(Color.white);
		g.fillRect((int) x, (int) y, 32, 32);
	}

	public Rectangle getBounds() {
		return new Rectangle((int)x, (int)y, 32, 32);
	}

}
