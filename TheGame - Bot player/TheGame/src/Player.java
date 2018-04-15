import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Player extends GameObject {

	private BufferedImage player_image_left;
	private BufferedImage player_image_right;
	
	
	public Player() {
		super(Game.WIDTH / 2 - 32, Game.HEIGHT / 2 - 32, ID.Player, Color.WHITE); // initialize player in the center of the screen.
		try {
			player_image_left = ImageIO.read(new File("resources/Player_Sprite_Left.png")); // load player left sprite.
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			player_image_right = ImageIO.read(new File("resources/Player_Sprite_Right.png")); // load player right
																								// sprite.
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		if (velX <= 0) {
			g.drawImage(player_image_left, (int) x, (int) y, null);
		} else {
			g.drawImage(player_image_right, (int) x, (int) y, null);
		}
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 32, 32);
	}

}
