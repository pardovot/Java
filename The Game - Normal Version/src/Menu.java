import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.Font;

/*
 *  Main mouse functions.
 */

public class Menu extends MouseAdapter {
	private Game game;
	private Handler handler;
	private Player player;
	private HUD hud = new HUD();

	public Menu(Game game, Handler handler, Player player) {
		this.game = game;
		this.handler = handler;
		this.player = player;
	}

	public void mousePressed(MouseEvent e) {
		int mx = e.getX();
		int my = e.getY();
		
		if(mouseHover(mx, my, 744, 9, 30, 31)) { // mute button.
			Game.mute = !Game.mute;
		}

		// menu screen.
		if (game.gameState == STATE.Menu) {

			// click to play.
			if (mouseHover(mx, my, (Game.WIDTH - 200) / 2, 230, 200, 64)) {
				if (!Game.mute) {
					Audio.getSound("menu_sound").play();
				}
				run();
			}

			// click to go to help screen.
			if (mouseHover(mx, my, (Game.WIDTH - 200) / 2, 330, 200, 64)) {
				game.gameState = STATE.Help;
				if (!Game.mute) {
					Audio.getSound("menu_sound").play();
					Audio.getSound("gameOver").play();
				}
			}

			// click to quit.
			if (mouseHover(mx, my, (Game.WIDTH - 200) / 2, 430, 200, 64)) {
				if (!Game.mute) {
					Audio.getSound("menu_sound").play();
				}
				System.exit(1);
			}
		}

		// help screen.
		if (game.gameState == STATE.Help) {

			// click to go back to menu.
			if (mouseHover(mx, my, 11, 17, 190, 24)) {
				game.gameState = STATE.Menu;
				if (!Game.mute) {
					Audio.getSound("menu_sound").play();
				}
			}
		}

		// game over screen.
		if (game.gameState == STATE.GameOver) {

			// click to play again.
			if (mouseHover(mx, my, 400 / 2, 230, 400, 64)) {
				if (!Game.mute) {
					Audio.getSound("menu_sound").play(); // plays menu sound.
				}
				run(); // resets the game values.
			}

			// click to go to menu.
			if (mouseHover(mx, my, (Game.WIDTH - 400) / 2, 330, 400, 64)) {
				if (!Game.mute) {
					Audio.getSound("menu_sound").play();
				}
				game.gameState = STATE.Menu;
			}

			// click to quit.
			if (mouseHover(mx, my, (Game.WIDTH - 400) / 2, 430, 400, 64)) {
				if (!Game.mute) {
					Audio.getSound("menu_sound").play();
				}
				System.exit(1);
			}
		}

		// winning screen.
		if (game.gameState == STATE.Winning) {

			// click to play again.
			if (mouseHover(mx, my, 400 / 2, 230, 400, 64)) {
				if (!Game.mute) {
					Audio.getSound("menu_sound").play();
				}
				run();
			}

			// click to go to menu.
			if (mouseHover(mx, my, (Game.WIDTH - 400) / 2, 330, 400, 64)) {
				if (!Game.mute) {
					Audio.getSound("menu_sound").play();
				}
				game.gameState = STATE.Menu;
			}

			// click to quit.
			if (mouseHover(mx, my, (Game.WIDTH - 400) / 2, 430, 400, 64)) {
				if (!Game.mute) {
					Audio.getSound("menu_sound").play();
				}
				System.exit(1);
			}
		}

		if (game.gameState == STATE.Game) {
			// click to go to menu.
			if (mouseHover(mx, my, 680, 17, 51, 15)) {
				if (!Game.mute) {
					Audio.getSound("menu_sound").play();
				}
				game.gameState = STATE.Menu;
				handler.object.clear();
				player.setX(Game.WIDTH / 2 - 32);
				player.setY(Game.HEIGHT / 2 - 32);
				player.setVelX(0);
				player.setVelY(0);
				game.paused = false;
			}
		}
	}

	public void run() { // run method, sets the default values to start a game, and initialize objects.
		game.gameState = STATE.Game;
		hud.resetGame();
		handler.addObject(player);
		handler.addObject(new basicEnemy());
		handler.addObject(new FollowingEnemy(player));
		game.setCurrentTime(System.currentTimeMillis());
		game.setExpectedTime(game.getCurrentTime() + game.getTime() * 1000);
	}

	private boolean mouseHover(int mx, int my, int x, int y, int width, int height) { // test to see if the mouse X,Y
																						// position is in desired
																						// position.
		if (mx > x && mx < x + width && my > y && my < y + height) {
			return true;
		} else {
			return false;
		}
	}

	public void render(Graphics g) { // menu graphics rendering method.
		if (game.gameState == STATE.Menu) { // menu screen rendering.

			Font font = new Font("Arial", 1, 70);
			Font font2 = new Font("Arial", 1, 50);

			g.setColor(Color.white);
			g.setFont(font);
			g.drawString("Menu", 310, 150);
			g.setFont(font2);
			g.drawString("Play", 345, 278);
			g.drawString("Help", 345, 378);
			g.drawString("Quit", 345, 478);
			g.drawRect((Game.WIDTH - 200) / 2, 230, 200, 64);

			g.setColor(Color.white);
			g.drawRect((Game.WIDTH - 200) / 2, 330, 200, 64);

			g.setColor(Color.white);
			g.drawRect((Game.WIDTH - 200) / 2, 430, 200, 64);

		} else if (game.gameState == STATE.GameOver) { // game over screen rendering.
			Font font = new Font("Arial", 1, 70);
			Font font2 = new Font("Small", 1, 20);
			Font font3 = new Font("Normal", 1, 50);
			g.setColor(Color.white);
			g.setFont(font);
			g.drawString("GAME OVER", 170, 150);
			g.setFont(font2);
			g.drawString("You reached level: " + (int) hud.getLevel(), 5, 25);
			g.drawString("Your score was: " + (int) hud.getScore(), 5, 50);

			g.setFont(font3);
			g.drawString("Play Again", 285, 278);
			g.drawString("Back to Menu", 245, 378);
			g.drawString("Quit", 345, 478);
			g.drawRect((Game.WIDTH - 400) / 2, 230, 400, 64);
			g.drawRect((Game.WIDTH - 400) / 2, 330, 400, 64);
			g.drawRect((Game.WIDTH - 400) / 2, 430, 400, 64);

		} else if (game.gameState == STATE.LevelUp) { // level up screen rendering.
			Font font = new Font("big", 1, 70);
			g.setFont(font);
			g.setColor(Color.WHITE);
			g.drawString("Level UP!", 235, 280);
			Font font2 = new Font("small", 1, 40);
			g.drawString("Level UP!", 235, 280);
			g.setFont(font2);
			g.drawString("Press space to continue", 175, 350);

		} else if (game.gameState == STATE.Help) { // help screen rendering.
			g.setColor(Color.white);
			Font font = new Font("small", 1, 30);
			g.setFont(font);
			g.drawString("Press 'P' to pause, 'M' to mute/unmute, 'Space' to play", 10, 90);
			font = new Font("Big", 1, 40);
			g.setFont(font);
			g.drawString("In this game your target is to avoid the", 40, 150);
			g.drawString("red and blue enemies:   ,", 145, 200);
			g.drawString("You do that by simply moving using the:", 20, 300);
			g.drawString("Left, Right, Up, Down arrow keys", 80, 350);
			g.drawString("There are also good objects that adds", 40, 450);
			g.drawString("Life:             and points:   ", 180, 500);
			font = new Font("small", 1, 30);
			g.setFont(font);
			g.drawString("Back to menu", 10, 40);
			g.setColor(Color.red);
			g.fillRect(565, 183, 16, 16);
			g.setColor(Color.blue);
			g.fillRect(600, 183, 16, 16);
			g.setColor(Color.yellow);
			g.fillRect(600, 484, 16, 16);
			g.setColor(Color.green);
			g.fillRect(272, 484, 16, 16);
		} else if (game.gameState == STATE.Winning) { // winning screen rendering.

			Font font = new Font("Big", 1, 70);
			Font font2 = new Font("Small", 1, 20);
			Font font3 = new Font("Normal", 1, 50);
			g.setColor(Color.white);
			g.setFont(font);
			g.drawString("YOU WIN!", 230, 150);
			g.setFont(font2);

			g.setFont(font3);
			g.drawString("Play Again", 285, 278);
			g.drawString("Back to Menu", 245, 378);
			g.drawString("Quit", 345, 478);
			g.drawRect((Game.WIDTH - 400) / 2, 230, 400, 64);
			g.drawRect((Game.WIDTH - 400) / 2, 330, 400, 64);
			g.drawRect((Game.WIDTH - 400) / 2, 430, 400, 64);
		}
	}
}
