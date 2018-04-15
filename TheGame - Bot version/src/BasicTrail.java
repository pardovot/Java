import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

/*
 * NOT USED IN THE BOT VERSION GAME.
 */

public class BasicTrail extends GameObject {

	private float alpha = 1;
	private float life;

	private Handler handler;
	private Color color;

	private int width, height;

	public BasicTrail(float x, float y, Color color, Handler handler) {
		super(x, y, ID.Trail);
		this.handler = handler;
		this.color = color;
		this.width = 16;
		this.height = 16;
		this.life = 0.08f;
	}

	public void tick() { // trail updating method.
		if (alpha > life) {
			alpha -= life - 0.01f; // substract alpha(trail) until life > alpha.
		} else {
			handler.removeObject(this);
		}
	}

	public void render(Graphics g) { // trail graphics rendering method.
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(makeTransparent(alpha));

		g.setColor(color);
		g.fillRect((int) x, (int) y, width, height);

		g2d.setComposite(makeTransparent(1));

	}

	private AlphaComposite makeTransparent(float alpha) { // Creates an AlphaComposite object with the specified rule
															// and the constant alpha to multiply with the alpha of the
															// source.
		int type = AlphaComposite.SRC_OVER;
		return (AlphaComposite.getInstance(type, alpha));
	}

	@Override
	public Rectangle getBounds() {
		return null;
	}
}
