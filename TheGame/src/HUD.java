import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class HUD {
	private static float health; // main player's health value. (default = 100);
	private static float score; // main player's score. (default = 0);
	private static float level; // main player's level. (default = 10;

	public void tick() { // head-up display updating method.
		health = Game.Bounds((int) health, 0, 100); // locks the health values to 0-100.
		score++; // adds score during the whole game.
	}

	public void render(Graphics g) { // head-up display graphics rendering method.
		g.setColor(Color.gray);
		g.fillRect(260, 15, 250, 32);
		try {
			Color color = new Color(255 - (int) (health * 2.55), (int) (health * 2.55), 0); // sets the color according
																							// to the health. (green to
																							// red).
			g.setColor(color);
		} catch (Exception e) {
		}
		g.fillRect(260, 15, (int) (health * 2.5), 32);
		g.setColor(Color.white);
		g.drawRect(260, 15, 250, 32);
		g.setFont(new Font("asd", Font.BOLD, 15));
		g.drawString("Level: " + (int) level, 5, 15);
		g.drawString("Score: " + (int) score, 5, 30);
	}

	public float getScore() {
		return score;
	}

	public void LevelUp() {
		level++;
	}

	public float getLevel() {
		return HUD.level;
	}

	public void goldenScore() { // adds 200 points to the score when taking golden score.
		HUD.score += 200;
	}

	public void addLife() { // adds 20 health points when taking life.
		if (HUD.health + 20 > 100) {
			HUD.setHealth(100);
		} else {
			HUD.health += 20;
		}
	}

	public static double getHealth() {
		return health;
	}

	public static void setHealth(float health) {
		HUD.health = health;
	}

	public void resetGame() { // resets the game to the default new game values.
		HUD.health = 100;
		HUD.score = 0;
		HUD.level = 1;
	}

}
