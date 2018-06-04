import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Bullet extends GameObject {

	private float frame = 0;
	private float animSpeed = 0.04f;
	private int frameCount = 16;
	private BufferedImage bigImage;
	private BufferedImage bullet;
	private float fixedDegree;

	public Bullet(float x, float y, ID id, Player player) {
		super(x, y, id);
		try {
			bigImage = ImageIO.read(new File("res/fire_blue.png"));
		} catch (Exception e) {
		}
		velX = (float) Math.cos((player.getDegree() - 90) * Game.degtorad) * 6;
		velY = (float) Math.sin((player.getDegree() - 90) * Game.degtorad) * 6;
		fixedDegree = player.getDegree();
	}

	public void tick() {
		x += velX;
		y += velY;
	}

	public void render(Graphics g) {
		frame += animSpeed;
		if (frame > frameCount)
			frame -= frameCount;
		try {
			bullet = bigImage.getSubimage((int) frame * 32, 0, 32, 50);
		} catch (Exception e) {
		}
		// if (!((x > Game.WIDTH) || (x < 0) || (y > Game.HEIGHT) || (y < 0))) {
		AffineTransform at = at(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(bullet, at, null);
		// }
		// g2d.drawImage(bullet, (int) x, (int) y, null);
	}

	public AffineTransform at(Graphics g) {
		AffineTransform at = AffineTransform.getTranslateInstance(x, y);
		at.rotate(Math.toRadians(fixedDegree), bullet.getWidth() / 2, bullet.getHeight() / 2);
		return at;
	}

}
