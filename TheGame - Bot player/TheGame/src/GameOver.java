import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class GameOver {
	private HUD hud = new HUD();
	
	public GameOver(HUD hud) {
		this.hud = hud;
	}
	
	public void render(Graphics g) { // displays the game over screen and level and score the player achieved.
		Font font = new Font("Arial", 1, 70);
		Font font2 = new Font("Small", 1, 20);
		Font font3 = new Font("Normal", 1, 50);
		g.setColor(Color.white);
		g.setFont(font);
		g.drawString("GAME OVER", 170, 150);
		g.setFont(font2);
		g.drawString("You reached level: " +(int) hud.getLevel(), 5, 25);
		g.drawString("Your score was: " +(int) hud.getScore(), 5, 50);
		
		g.setFont(font3);
		g.drawString("Play Again", 285, 278);
		g.drawString("Back to Menu", 245, 378);
		g.drawString("Quit", 345, 478);
		g.drawRect((Game.WIDTH - 400) / 2, 230, 400, 64);
		g.drawRect((Game.WIDTH - 400) / 2, 330, 400, 64);
		g.drawRect((Game.WIDTH - 400) / 2, 430, 400, 64);
	}
	
	
}
