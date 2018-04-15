import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/*
 *  Main keyboard functions.
 */

public class KeyInput extends KeyAdapter {
	private Handler handler;
	private Game game;
	private HUD hud = new HUD();
	private Player player;
	private basicEnemy basicEnemy;
	private FollowingEnemy followingEnemy;
	
	public KeyInput(Handler handler, Game game, Player player, basicEnemy basicEnemy,
			FollowingEnemy followingEnemy) {
		this.handler = handler;
		this.game = game;
		this.player = player;
		this.basicEnemy = basicEnemy;
		this.followingEnemy = followingEnemy;
	}

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		for (int i = 0; i < handler.object.size(); i++) { // goes over all the objects and settings up the player speed.
			try {
				GameObject tempObject = handler.object.get(i);
				if (tempObject.getId() == ID.Player) {
					if (key == KeyEvent.VK_UP)
						tempObject.setVelY(tempObject.getVelY() - 5);
					if (key == KeyEvent.VK_LEFT)
						tempObject.setVelX(tempObject.getVelX() - 5);
					if (key == KeyEvent.VK_DOWN)
						tempObject.setVelY(tempObject.getVelY() + 5);
					if (key == KeyEvent.VK_RIGHT)
						tempObject.setVelX(tempObject.getVelX() + 5);
					tempObject.setVelY(Game.Bounds(tempObject.getVelY(), -5, 5));
					tempObject.setVelX(Game.Bounds(tempObject.getVelX(), -5, 5));
				}
			} catch (NullPointerException ex) {
			}
		}
		if (key == KeyEvent.VK_ESCAPE) // exits game if esc is pressed.
			System.exit(1);

		if(game.gameState == STATE.Game) { // pause/unpause function for the game.
			if(key == KeyEvent.VK_P) {
				if(game.paused) {
					game.paused = false;
				} else {
					game.paused = true;
				}
			}
			if(key == KeyEvent.VK_E) {
				System.out.println("asd");
				handler.addObject(new basicEnemy());
			}
		}
		if (game.gameState == STATE.Menu || game.gameState == STATE.GameOver) { // starts the game if pressed 'space' on the menu.
			if (key == KeyEvent.VK_SPACE) {
				game.gameState = STATE.Game;
				hud.resetGame();
				handler.addObject(player);
				handler.addObject(basicEnemy);
				handler.addObject(new BasicTrail(basicEnemy.getX(), basicEnemy.getY(), Color.RED, handler));
				handler.addObject(new FollowingEnemy(player));
			}
		}
		if (game.gameState == STATE.LevelUp) { // goes to next level after 'space' is pressed.
			if (key == KeyEvent.VK_SPACE) {
				game.gameState = STATE.Game;
				handler.addObject(followingEnemy);
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		for (int i = 0; i < handler.object.size(); i++) { // opposite directions from the 'keyPressed' function, to make the player stop on release.
			try {
				GameObject tempObject = handler.object.get(i);
				if (tempObject.getId() == ID.Player) {
					if (key == KeyEvent.VK_UP)
						tempObject.setVelY(tempObject.getVelY() + 5);
					if (key == KeyEvent.VK_LEFT)
						tempObject.setVelX(tempObject.getVelX() + 5);
					if (key == KeyEvent.VK_DOWN)
						tempObject.setVelY(tempObject.getVelY() - 5);
					if (key == KeyEvent.VK_RIGHT)
						tempObject.setVelX(tempObject.getVelX() - 5);
					tempObject.setVelY(Game.Bounds(tempObject.getVelY(), -5, 5));
					tempObject.setVelX(Game.Bounds(tempObject.getVelX(), -5, 5));
				}
			} catch (NullPointerException ex) {
			}
		}
	}
	
	public void render(Graphics g) {

	}
}
